package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "z_the_entity")
class ZEntity (
    @Column(nullable = false)
    var name: String
) : Identifiable()