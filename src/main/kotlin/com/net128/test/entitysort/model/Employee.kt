package com.net128.test.entitysort.model

import javax.persistence.*

@Entity
@Table(name = "employee")
class Employee (
    @Column(nullable = false)
    var name: String,

    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    @ManyToOne
    var address2: Address2
) : Identifiable()