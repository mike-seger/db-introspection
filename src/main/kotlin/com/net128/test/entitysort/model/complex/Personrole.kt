package com.net128.test.entitysort.model.complex

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table
import org.springframework.core.style.ToStringCreator
import java.io.Serializable

@Entity
@IdClass(PersonrolePK::class)
@Table(name = "PERSONROLE")
data class Personrole(
    @Id
    @Column(name = "PERSON_ID", nullable = false)
    var personId: Int? = null,

    @Id
    @Column(name = "ROLE_ID", nullable = false)
    var roleId: Int? = null
)

data class PersonrolePK(
    var personId: Int? = null,
    var roleId: Int? = null
) : Serializable