package com.example.demo.dao


import com.example.demo.model.MedicalTeam
import org.springframework.data.jpa.repository.JpaRepository

interface TeamsRepository : JpaRepository<MedicalTeam, Int> {

}