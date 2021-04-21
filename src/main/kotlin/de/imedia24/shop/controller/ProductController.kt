package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.dto.error.ErrorDTO
import de.imedia24.shop.dto.product.ProductDTO
import de.imedia24.shop.service.ProductService
import javassist.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.json.JsonMergePatch
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException


@RestController
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateKeyException::class, ConstraintViolationException::class)
    @ResponseBody
    fun handleBadRequest(req: HttpServletRequest, ex: Exception): ErrorDTO? {
        return createErrorDTO(ex, req)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    @ResponseBody
    fun handleNotFound(req: HttpServletRequest, ex: Exception): ErrorDTO? {
        return createErrorDTO(ex, req)
    }

    private fun createErrorDTO(
        ex: Exception,
        req: HttpServletRequest
    ): ErrorDTO {
        val exceptionType = ex.javaClass.name
        return ErrorDTO(
            ex.message!!,
            exceptionType.substring(exceptionType.lastIndexOf(".") + 1).replace("Exception", "Error"),
            req.getRequestURL().toString()
        )
    }


    @GetMapping("/products/{sku}", produces = ["application/json;charset=utf-8"])
    fun findProductsBySku(
        @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        val product = productService.findProductBySku(sku)
        return ResponseEntity.ok(product)
    }

    @GetMapping("/products", produces = ["application/json;charset=utf-8"])
    fun findProductsBySkus(
        @RequestParam("skus") skus: List<String>
    ): ResponseEntity<List<ProductResponse>> {
        val products = productService.findProductsBySkus(skus)
        return ResponseEntity.ok(products)
    }

    @PostMapping(
        "/products",
        consumes = ["application/json;charset=utf-8"],
        produces = ["application/json;charset=utf-8"]
    )
    fun addProduct(
        @RequestBody product: ProductDTO
    ): ResponseEntity<ProductResponse> {
        try {
            return ResponseEntity.ok(productService.saveProduct(product))
        } catch (e: DataIntegrityViolationException) {
            val error = "There is already a product with sku = ${product.sku}"
            logger.error(error, e)
            ResponseEntity.badRequest()
            throw DuplicateKeyException(error)
        }
    }

    @PatchMapping(
        "/products/{sku}",
        consumes = ["application/merge-patch+json"],
        produces = ["application/json;charset=utf-8"]
    )
    fun patchProduct(
        @PathVariable("sku") sku: String,
        @RequestBody mergePatchDocument: JsonMergePatch
    ): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(productService.patchProduct(sku, mergePatchDocument))
    }


}
