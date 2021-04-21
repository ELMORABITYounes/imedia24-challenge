package de.imedia24.shop.db.entity

import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.domain.Persistable
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.PrePersist
import javax.persistence.PostLoad
import kotlin.jvm.Transient


@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    @Column(name = "sku", nullable = false)
    val sku: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "price", nullable = false)
    var price: BigDecimal,

    @Column(name = "stock_level", nullable = false)
    var stockLevel: Int,

    @UpdateTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: ZonedDateTime,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    var updatedAt: ZonedDateTime
) : Persistable<String> {

    @Transient
    var new = false

    override fun isNew(): Boolean {
        return new
    }

    override fun getId(): String {
        return sku
    }

    @PrePersist
    @PostLoad
    fun markNotNew() {
        new = false
    }

}
