package com.net128.test.entitysort.model

import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Customer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val username: String,
    val password: String,
    val email: String,
    @OneToOne(cascade = [CascadeType.ALL])
    val profile: UserProfile
)

@Entity
data class UserProfile(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val firstName: String,
    val lastName: String,
    @OneToOne(mappedBy = "profile", cascade = [CascadeType.ALL])
    val address: Address,
    @OneToOne(mappedBy = "profile", cascade = [CascadeType.ALL])
    val paymentDetail: PaymentDetail
)

@Entity
data class PaymentDetail(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val creditCardNumber: String,
    @OneToOne
    val profile: UserProfile
)

@Entity
data class Address(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val street: String,
    val city: String,
    @ManyToOne
    val state: State,
    @ManyToOne
    val country: Country,
    @OneToOne
    val profile: UserProfile? = null
)

@Entity
data class State(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @OneToMany(mappedBy = "state")
    val cities: List<Address> = mutableListOf()
)

@Entity
data class Country(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: Char, // Assuming single char representation like 'U' for USA
    @OneToMany(mappedBy = "country")
    val addresses: List<Address> = mutableListOf()
)

@Entity
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    @ManyToOne
    val category: Category,
    @ManyToOne
    val brand: Brand,
    @ManyToMany
    @JoinTable(name = "product_tag")
    val tags: List<Tag> = mutableListOf()
)

@Entity
data class Brand(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val description: String,
    @OneToMany(mappedBy = "brand")
    val products: List<Product> = mutableListOf()
)

@Entity
data class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @OneToMany(mappedBy = "category")
    val products: List<Product> = mutableListOf(),
    @ManyToOne
    val parentCategory: Category? = null,
    @OneToMany(mappedBy = "parentCategory")
    val subCategories: List<Category> = mutableListOf()
)

@Entity
data class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val rating: Int,
    val comment: String,
    @ManyToOne
    val product: Product,
    @ManyToOne
    val customer: Customer
)

@Entity
data class Cart(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @OneToOne
    val customer: Customer,
    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL])
    val items: List<CartItem> = mutableListOf()
)

@Entity
data class CartItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val quantity: Int,
    @ManyToOne
    val product: Product,
    @ManyToOne
    val cart: Cart
)

@Entity
data class CustomerOrder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val date: String,
    val totalAmount: BigDecimal,
    @ManyToOne
    val customer: Customer,
    @OneToMany(mappedBy = "customerOrder", cascade = [CascadeType.ALL])
    val orderItems: List<OrderItem> = mutableListOf(),
    @Enumerated(EnumType.STRING)
    val status: OrderStatus
)

@Entity
data class OrderItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val quantity: Int,
    @ManyToOne
    val product: Product,
    @ManyToOne
    val customerOrder: CustomerOrder
)

@Entity
data class Payment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val amount: BigDecimal,
    val date: String,
    @Enumerated(EnumType.STRING)
    val paymentType: PaymentType,
    @OneToOne
    val customerOrder: CustomerOrder
)

@Entity
data class Supplier(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(length = 64)
    val name: String,
    @ManyToMany
    @JoinTable(name = "supplier_product")
    val products: List<Product> = mutableListOf()
)

@Entity
data class Inventory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val stockCount: Int,
    @OneToOne
    val product: Product,
    @ManyToOne
    val warehouse: Warehouse
)

@Entity
data class Shipment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @OneToOne
    @JoinColumn(name = "customer_order_id")
    val customerOrder: CustomerOrder,
    @Column(length = 16)
    val trackingNumber: String,
    @Enumerated(EnumType.STRING)
    val status: ShipmentStatus
)

@Entity
data class Tag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @Column(length = 128)
    val description: String,
    @ManyToMany(mappedBy = "tags")
    val products: List<Product> = mutableListOf()
)

@Entity
data class Discount(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val code: String,
    val percentage: Double,
    val startDate: LocalDate,
    val endDate: LocalDate,
    @ManyToMany
    @JoinTable(name = "discount_product")
    val applicableProducts: List<Product> = mutableListOf()
)

@Entity
data class Wishlist(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @OneToOne
    val customer: Customer,
    @ManyToMany
    @JoinTable(name = "wishlist_product")
    val products: List<Product> = mutableListOf()
)

@Entity
data class Manufacturer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(length = 32)
    val name: String,
    @ManyToMany
    @JoinTable(name = "manufacturer_product")
    val products: List<Product> = mutableListOf()
)

@Entity
data class Warehouse(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(length = 32)
    val name: String,
    @Column(length = 64)
    val location: String,
    @OneToMany(mappedBy = "warehouse")
    val inventories: List<Inventory> = mutableListOf()
)

enum class PaymentType {
    CREDIT_CARD, DEBIT_CARD, PAYPAL, CASH_ON_DELIVERY
}

enum class OrderStatus {
    PENDING, SHIPPED, DELIVERED, CANCELLED
}

enum class ShipmentStatus {
    IN_TRANSIT, DELIVERED, RETURNED
}
