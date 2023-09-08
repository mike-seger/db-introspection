package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "f_the_entity")
class FEntity (
    @Column(nullable = false)
    var name: String
) : Identifiable()