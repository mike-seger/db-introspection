package com.net128.test.entitysort.model.blog

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "post_details")
class PostDetails {
    @Id
    @GeneratedValue
    private val id: Long? = null

    @Column(name = "created_on")
    private val createdOn: Date? = null

    @Column(name = "created_by")
    private val createdBy: String? = null

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private val post: Post? = null //Getters and setters omitted for brevity
}