package com.net128.test.entitysort.model.blog

import javax.persistence.*


@Entity
@Table(name = "post_comment")
class PostComment {
    @Id
    private val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    private val post: Post? = null
    private val review: String? = null //Getters and setters omitted for brevity
}