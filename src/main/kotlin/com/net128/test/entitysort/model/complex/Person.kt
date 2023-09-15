package com.net128.test.entitysort.model.complex

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "PERSON")
data class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PERSON_ID", unique = true, nullable = false)
    var id: Int? = null,

    @Column(name = "ACTIVE_IND", nullable = false)
    var activeIndicator: Int = 0,

    @Column(name = "EMAIL", unique = true, nullable = false, length = 128)
    var email: String? = null,

    @Column(name = "FIRSTNAME", nullable = false, length = 45)
    var firstname: String? = null,

    @Column(name = "LASTNAME", nullable = false, length = 45)
    var lastname: String? = null,

    @Column(name = "PASSWORD", nullable = false, length = 45)
    var password: String? = null,

    @Column(name = "PHONE_SMS", precision = 10)
    var phoneSms: BigDecimal? = null,

    @Column(name = "USERNAME", unique = true, nullable = false, length = 45)
    var username: String? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "PERSONROLE",
        joinColumns = [JoinColumn(name = "PERSON_ID", nullable = false)],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID", nullable = false)]
    )
    var roles: Set<Role>? = null
)