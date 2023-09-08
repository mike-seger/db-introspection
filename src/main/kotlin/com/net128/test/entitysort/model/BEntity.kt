package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "b_the_entity")
class BEntity (
    @Column(nullable = false)
    var name: String
) : Identifiable()