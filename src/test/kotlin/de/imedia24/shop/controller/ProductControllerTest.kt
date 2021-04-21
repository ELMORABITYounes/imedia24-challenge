package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.service.ProductService
import javassist.NotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import java.math.BigDecimal


@SpringBootTest
internal class ProductControllerTest {

    @Autowired
    lateinit var productController: ProductController

    @MockBean
    lateinit var productService: ProductService

    @Test
    fun findProductsBySkus_WithServiceReturnResults() {
        val skus = listOf<String>("sk1", "sku2")
        val results = listOf<ProductResponse>(
            ProductResponse(
                "sk1", "sk1", "sk1",
                BigDecimal(10), 1
            ), ProductResponse(
                "sk2", "sk2", "sk2",
                BigDecimal(10), 1
            )
        )
        BDDMockito.given(productService.findProductsBySkus(skus)).willReturn(results)
        assertSame(HttpStatus.OK, productController.findProductsBySkus(skus).statusCode)
        assertSame(results, productController.findProductsBySkus(skus).body)
    }

    @Test()
    fun findProductsBySkus_WithServiceThrowsException() {
        val skus = listOf<String>("sk1", "sku2")
        BDDMockito.given(productService.findProductsBySkus(skus)).willAnswer({ throw NotFoundException("test") })
        val exception = Assertions.assertThrows(NotFoundException::class.java) {
            productController.findProductsBySkus(skus)
        }
        assertEquals("test", exception.message)
    }
}