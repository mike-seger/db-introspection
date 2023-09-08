package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "address")
class Address (
    @Column(name = "address_id", nullable = false, unique = true)
    var addressId: String,

    @Column(nullable = false)
    var street: String,

    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    @ManyToOne
    var city: City
) : Identifiable()