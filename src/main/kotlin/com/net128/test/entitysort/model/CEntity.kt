package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "c_the_entity")
class CEntity (
    @Column(nullable = false)
    var name: String
) : Identifiable()