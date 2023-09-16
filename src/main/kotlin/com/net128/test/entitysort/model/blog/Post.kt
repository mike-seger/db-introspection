package com.net128.test.entitysort.model.blog

import org.hibernate.annotations.LazyToOne
import org.hibernate.annotations.LazyToOneOption
import javax.persistence.*


@Entity
@Table(name = "post")
class Post {
    @Id
    private val id: Long? = null
    private val title: String? = null

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val comments: List<PostComment> = ArrayList<PostComment>()

    @OneToOne(mappedBy = "post", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private val details: PostDetails? = null

    @ManyToMany
    @JoinTable(
        name = "post_tag",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    private val tags: List<Tag> = ArrayList() //Getters and setters omitted for brevity
}