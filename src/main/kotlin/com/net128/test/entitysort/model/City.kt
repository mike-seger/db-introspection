package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "city")
class City (
    @Column(name="city_id", nullable = false, unique = true)
    var cityId: String,

    @Column(nullable = false)
    var name: String
) : Identifiable()