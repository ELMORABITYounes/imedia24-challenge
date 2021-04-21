package de.imedia24.shop.dto.product

import de.imedia24.shop.db.entity.ProductEntity
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ProductDTO(
    @field:NotNull
    @field:NotBlank
    val sku: String,
    @field:NotNull
    @field:NotBlank
    val name: String,
    val description: String?,
    @field:NotNull
    val price: BigDecimal,
    @field:NotNull
    val stockLevel: Int
) {
    companion object {
        fun ProductEntity.toProductDTO() = ProductDTO(
            sku = sku,
            name = name,
            description = description,
            price = price,
            stockLevel = stockLevel
        )
    }
}