package com.example.demo.dao


import com.example.demo.model.Doctor
import org.springframework.data.jpa.repository.JpaRepository

interface DoctorsRepository : JpaRepository<Doctor, Int> {

}