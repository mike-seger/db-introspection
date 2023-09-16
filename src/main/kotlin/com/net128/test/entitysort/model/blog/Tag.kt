package com.net128.test.entitysort.model.blog

import javax.persistence.Entity
import javax.persistence.Id


@Entity
class Tag {
    @Id
    private val id: Long? = null
    private val name: String? = null //Getters and setters omitted for brevity
}