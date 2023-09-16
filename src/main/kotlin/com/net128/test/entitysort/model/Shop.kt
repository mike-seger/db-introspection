package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "`USER`")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val username: String,
    val password: String, // In a real scenario, never save plain text password. Instead, store hashed ones.
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
    val dateOfBirth: String,
    @OneToOne(mappedBy = "profile")
    val user: User,
    @OneToOne(cascade = [CascadeType.ALL])
    val address: Address
)

@Entity
data class Address(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    @OneToOne(mappedBy = "address")
    val profile: UserProfile,
    @ManyToOne
    val country: Country
)

@Entity
data class Country(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @OneToMany(mappedBy = "country")
    val addresses: List<Address> = mutableListOf()
)

@Entity
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    @ManyToOne
    val category: Category,
    @OneToMany(mappedBy = "product")
    val reviews: List<Review> = mutableListOf(),
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    val supplier: Supplier
)

@Entity
data class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @OneToMany(mappedBy = "category")
    val products: List<Product> = mutableListOf()
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
    val user: User
)

@Entity
data class Cart(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @OneToOne
    val user: User,
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
@Table(name = "`ORDER`")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val date: String,
    val totalAmount: Double,
    @ManyToOne
    val user: User,
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
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
    val order: Order
)

@Entity
data class Payment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val amount: Double,
    val date: String,
    @Enumerated(EnumType.STRING)
    val paymentType: PaymentType,
    @OneToOne
    val order: Order
)

enum class PaymentType {
    CREDIT_CARD, DEBIT_CARD, PAYPAL, CASH_ON_DELIVERY
}

enum class OrderStatus {
    PENDING, SHIPPED, DELIVERED, CANCELLED
}

@Entity
data class Supplier(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @OneToMany(mappedBy = "supplier")
    val products: List<Product> = mutableListOf()
)

@Entity
data class Inventory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @OneToOne
    val product: Product,
    val stockCount: Int
)

@Entity
data class Shipment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @OneToOne
    val order: Order,
    val trackingNumber: String,
    @Enumerated(EnumType.STRING)
    val status: ShipmentStatus
)

enum class ShipmentStatus {
    IN_TRANSIT, DELIVERED, RETURNED
}

@Entity
data class Discount(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val code: String,
    val percentage: Double,
    val startDate: String,
    val endDate: String,
    @ManyToMany
    @JoinTable(
        name = "discount_product",
        joinColumns = [JoinColumn(name = "discount_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    val applicableProducts: List<Product> = mutableListOf()
)

@Entity
data class Wishlist(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @OneToOne
    val user: User,
    @ManyToMany
    @JoinTable(
        name = "wishlist_product",
        joinColumns = [JoinColumn(name = "wishlist_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    val products: List<Product> = mutableListOf()
)
