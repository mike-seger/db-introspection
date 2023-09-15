package com.net128.test.entitysort.model.complex

import javax.persistence.*
import org.springframework.core.style.ToStringCreator

@Entity
@Table(name = "COURSE")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COURSE_ID", unique = true, nullable = false)
    var id: Int? = null,

    @Column(name = "COURSE_CD", unique = true, nullable = false, length = 45, updatable = true, insertable = true)
    var code: String? = null,

    @Column(name = "COURSE_DESC", length = 512, updatable = true, insertable = true)
    var description: String? = null,

    @Column(name = "COURSE_NM", nullable = false, length = 45, updatable = true, insertable = true)
    var name: String? = null
)