package com.net128.test.entitysort.model.complex

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import org.springframework.core.style.ToStringCreator

@Entity
@Table(name = "ROLE")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLE_ID", unique = true, nullable = false, updatable = false, insertable = false)
    var id: Int? = null,

    @Column(name = "ROLE_NM", nullable = false, length = 128, updatable = false, insertable = false)
    var name: String? = null,

    @Column(name = "ROLE_CD", nullable = false, length = 45, updatable = false, insertable = false)
    var code: String? = null
)