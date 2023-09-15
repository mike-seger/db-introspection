package com.net128.test.entitysort.model.complex

import javax.persistence.*

@Entity
@Table(name = "CLASS")
data class Clss(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CLASS_ID", unique = true, nullable = false, updatable = true, insertable = true)
    var id: Int? = null,

    @Column(name = "CLASS_CD", unique = true, nullable = false, length = 45, updatable = true, insertable = true)
    var code: String? = null,

    @Column(name = "CLASS_DESC", length = 512, updatable = true, insertable = true)
    var description: String? = null,

    @Column(name = "CLASS_NM", nullable = false, length = 45, updatable = true, insertable = true)
    var name: String? = null,

    // uni-directional many-to-one association to Course
    @ManyToOne
    @JoinColumn(name = "COURSE_ID", nullable = false, updatable = true, insertable = true)
    var course: Course? = null
)