package com.example.demo.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class MedicalTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var name: String? = null

    constructor() {}

    constructor(id: Int?, name: String?) {
        this.id = id
        this.name = name
    }

}