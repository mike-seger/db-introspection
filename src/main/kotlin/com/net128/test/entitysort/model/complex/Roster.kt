package com.net128.test.entitysort.model.complex

import javax.persistence.*

@Entity
@Table(name = "ROSTER")
data class Roster(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROSTER_ID", unique = true, nullable = false)
    var id: Int? = null,

    @Column(name = "ATTEND_IND", nullable = false)
    var attendanceIndicator: Boolean = false,

    // uni-directional one-to-many association to Classcomment
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "ROSTER_ID", referencedColumnName = "ROSTER_ID")
    @OrderBy("CLASSCOMMENT_ID asc")
    var comments: MutableList<Classcomment>? = null,

    // uni-directional many-to-one association to Classinstance
    @ManyToOne
    @JoinColumn(name = "CLASSINSTANCE_ID", nullable = false)
    var classinstance: Classinstance? = null,

    // uni-directional many-to-one association to Person
    @ManyToOne
    @JoinColumn(name = "PERSON_ID", nullable = false)
    var student: Person? = null
)