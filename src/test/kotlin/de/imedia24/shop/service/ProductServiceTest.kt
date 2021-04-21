package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductResponse
import javassist.NotFoundException
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.math.BigDecimal
import java.time.ZonedDateTime

@SpringBootTest
internal class ProductServiceTest {

    @Autowired
    lateinit var productService: ProductService

    @MockBean
    lateinit var productRepository: ProductRepository

    @Test
    fun findProductsBySkus_whenAllProductsExists() {
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
        BDDMockito.given(productRepository.findBySku("sk1")).willReturn(
            ProductEntity(
                "sk1", "sk1", "sk1",
                BigDecimal(10), 1,
                ZonedDateTime.now
                    (), ZonedDateTime.now()
            )
        )
        BDDMockito.given(productRepository.findBySku("sku2")).willReturn(
            ProductEntity(
                "sk2", "sk2", "sk2",
                BigDecimal(10), 1,
                ZonedDateTime.now
                    (), ZonedDateTime.now()
            )
        )
        assertEquals(results, productService.findProductsBySkus(skus))
    }

    @Test
    fun findProductsBySkus_whenOneProductNotFound() {
        val skus = listOf<String>("sk1", "sku2")
        BDDMockito.given(productRepository.findBySku("sk1")).willReturn(
            ProductEntity(
                "sk1", "sk1", "sk1",
                BigDecimal(10), 1,
                ZonedDateTime.now
                    (), ZonedDateTime.now()
            )
        )
        BDDMockito.given(productRepository.findBySku("sku2")).willReturn(
            null
        )
        val exception = assertThrows(NotFoundException::class.java) {
            productService.findProductsBySkus(skus)
        }
        assertEquals("No product with sku sku2 found", exception.message)
    }
}