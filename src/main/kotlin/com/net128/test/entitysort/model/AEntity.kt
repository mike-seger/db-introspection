package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "a_the_entity")
class AEntity (
    @Column(nullable = false)
    var name: String
) : Identifiable()