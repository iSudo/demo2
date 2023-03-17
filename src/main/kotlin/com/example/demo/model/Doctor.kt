package com.example.demo.model

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var firstName: String? = null
    var lastName: String? = null

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var birthDate: LocalDate? = null
    var medicalTeamId: Int? = null

    constructor() {}

    constructor(id: Int?, firstName: String?, lastName: String?, birthDate: LocalDate?, medicalTeamId: Int?) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.birthDate = birthDate
        this.medicalTeamId = medicalTeamId
    }

    override fun toString(): String {
        return "Doctor(id=$id, firstName=$firstName, lastName=$lastName, birthDate=$birthDate, medicalTeamId=$medicalTeamId)"
    }

}