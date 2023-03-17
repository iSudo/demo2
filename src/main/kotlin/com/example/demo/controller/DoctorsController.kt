package com.example.demo.controller

import com.example.demo.dao.DoctorsRepository
import com.example.demo.model.Doctor
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/doctors")
class DoctorsController(val employeeRepository: DoctorsRepository) {

    @GetMapping("/list")
    fun getAll(): List<Doctor> {
        return employeeRepository.findAll()
    }

    @PostMapping("/addOrUpdate")
    fun addOrUpdate(doc: Doctor): Doctor {
        return employeeRepository.save(doc)
    }

    @PostMapping("/remove")
    fun remove(id: Int) {
        employeeRepository.deleteById(id)
    }

}