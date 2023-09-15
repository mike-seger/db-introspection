package com.net128.test.entitysort.model.complex

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import org.springframework.core.style.ToStringCreator

@Entity
@Table(name = "ROOM")
data class Room(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROOM_ID", unique = true, nullable = false)
    var id: Int? = null,

    @Column(name = "ROOM_CD", nullable = false, length = 45)
    var code: String,

    @Column(name = "FLOOR_NBR", nullable = false)
    var floor: Int,

    @Column(name = "ROOM_NM", nullable = false, length = 45)
    var name: String,

    @Column(name = "ROOM_DESC", nullable = true, length = 512)
    var description: String? = null,

    @Column(name = "ROOM_SEATS_NBR", nullable = false)
    var numberOfSeats: Int
)