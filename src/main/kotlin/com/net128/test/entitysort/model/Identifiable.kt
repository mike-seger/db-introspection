package com.net128.test.entitysort.model

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class Identifiable(newId: Long? = null) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = newId
}