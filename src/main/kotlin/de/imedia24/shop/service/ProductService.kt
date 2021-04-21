package de.imedia24.shop.service

import de.imedia24.shop.service.utils.PatchUtils
import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.dto.product.ProductDTO
import de.imedia24.shop.dto.product.ProductDTO.Companion.toProductDTO
import javassist.NotFoundException
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.json.JsonMergePatch
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Service
class ProductService(private val productRepository: ProductRepository, val validator: Validator, val patchUtils: PatchUtils) {

    fun findProductBySku(sku: String): ProductResponse {
        val productEntity =
            productRepository.findBySku(sku) ?: throw NotFoundException("No product with sku $sku found")
        return productEntity.toProductResponse()
    }

    fun findProductsBySkus(skus: List<String>): List<ProductResponse> {
        val products = arrayListOf<ProductResponse>()
        for (sku in skus) {
            val productEntity =
                productRepository.findBySku(sku) ?: throw NotFoundException("No product with sku $sku found")
            products.add(productEntity.toProductResponse())
        }
        return products
    }

    fun saveProduct(product: ProductDTO): ProductResponse {
        val violations = validator.validate(product)

        if (!violations.isEmpty()) {
            throw ConstraintViolationException(violations)
        }
        val productEntity =
            ProductEntity(
                product.sku, product.name, product.description, product.price, product.stockLevel,
                ZonedDateTime.now(), ZonedDateTime.now()
            )
        productEntity.new = true
        return productRepository.save(productEntity).toProductResponse()
    }

    fun patchProduct(sku: String, mergePatchDocument: JsonMergePatch): ProductResponse {
        val productEntity = productRepository.findBySku(sku)
        if (productEntity != null) {
            val productDTO = productEntity.toProductDTO()
            val patchedProduct = patchUtils.mergePatch(mergePatchDocument, productDTO, ProductDTO::class.java)
            productEntity.name = patchedProduct.name
            productEntity.description = patchedProduct.description
            productEntity.price = patchedProduct.price
            productEntity.stockLevel = patchedProduct.stockLevel
            productEntity.updatedAt = ZonedDateTime.now()
            return productRepository.save(productEntity).toProductResponse()
        } else {
            throw NotFoundException("No product with sku $sku found")
        }
    }
}
