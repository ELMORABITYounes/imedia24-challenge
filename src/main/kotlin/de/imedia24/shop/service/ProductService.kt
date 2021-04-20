package de.imedia24.shop.service

import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun findProductBySku(sku: String): ProductResponse? {
        val productEntity = productRepository.findBySku(sku) ?: return null
        return productEntity.toProductResponse()
    }

    fun findProductsBySkus(skus: List<String>): List<ProductResponse> {
        val products = arrayListOf<ProductResponse>()
        for (sku in skus) {
            val productEntity = productRepository.findBySku(sku)
            if (productEntity!=null){
                products.add(productEntity.toProductResponse())
            }
        }
        return products
    }
}
