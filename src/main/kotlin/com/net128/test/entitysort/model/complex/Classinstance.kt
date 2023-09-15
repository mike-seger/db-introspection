package com.net128.test.entitysort.model.complex

import java.util.Date
import javax.persistence.*
import org.springframework.format.annotation.DateTimeFormat

@Entity
@Table(name = "CLASSINSTANCE")
data class Classinstance(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CLASSINSTANCE_ID", unique = true, nullable = false)
    var id: Int? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLASSINSTANCE_DT", nullable = false)
    @DateTimeFormat(style = "SS")
    var dateTime: Date? = null,

    @Column(name = "CLASSINSTANCE_MINS", nullable = false)
    var durationMinutes: Int = 0,

    @Column(name = "CLASSINSTANCE_NOTE", length = 512)
    var note: String? = null,

    // uni-directional many-to-one association to Class
    @ManyToOne
    @JoinColumn(name = "CLASS_ID", nullable = false)
    var clss: Clss? = null,

    // uni-directional many-to-one association to Person
    @ManyToOne
    @JoinColumn(name = "INSTRUCTOR_ID", nullable = false)
    var instructor: Person? = null,

    // uni-directional many-to-one association to Room
    @ManyToOne
    @JoinColumn(name = "ROOM_ID", nullable = false)
    var room: Room? = null
)