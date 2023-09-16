package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "address2")
class Address2 (
    @Column(name = "address_id", nullable = false, unique = true)
    var addressId: String,

    @Column(nullable = false)
    var street: String,

    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    @ManyToOne
    var city: City
) : Identifiable()


@Entity
@Table(name = "city")
class City (
    @Column(name="city_id", nullable = false, unique = true)
    var cityId: String,

    @Column(nullable = false)
    var name: String
) : Identifiable()

@Entity
@Table(name = "employee")
class Employee (
    @Column(nullable = false)
    var name: String,

    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    @ManyToOne
    var address2: Address2
) : Identifiable()

@MappedSuperclass
open class Identifiable(newId: Long? = null) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = newId
}