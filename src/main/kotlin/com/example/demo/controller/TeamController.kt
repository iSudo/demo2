package com.example.demo.controller

import com.example.demo.dao.TeamsRepository
import com.example.demo.model.MedicalTeam
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/teams")
class TeamController(val teamsRepository: TeamsRepository) {

    @GetMapping("/list")
    fun getAll(): List<MedicalTeam> {
        return teamsRepository.findAll()
    }

    @PostMapping("/addOrUpdate")
    fun addOrUpdate(team: MedicalTeam): MedicalTeam {
        return teamsRepository.save(team)
    }

    @PostMapping("/remove")
    fun remove(id: Int) {
        teamsRepository.deleteById(id)
    }

}