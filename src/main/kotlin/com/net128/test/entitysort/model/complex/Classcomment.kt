package com.net128.test.entitysort.model.complex

import javax.persistence.*
import org.springframework.core.style.ToStringCreator

@Entity
@Table(name = "CLASSCOMMENT")
data class Classcomment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLASSCOMMENT_ID", unique = true, nullable = false)
    var id: Int? = null,

    @Column(name = "CLASSCOMMENT_TX", nullable = false, length = 1024)
    var comment: String? = null,

    @Column(name = "SCORE_INT")
    var score: Int? = null
)