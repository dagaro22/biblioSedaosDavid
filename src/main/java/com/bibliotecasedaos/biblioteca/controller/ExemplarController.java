/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.service.ExemplarService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dg
 */
@RequestMapping("/biblioteca/exemplars")
@RestController
public class ExemplarController {
    
    @Autowired
    ExemplarService exemplarService;
    
    @GetMapping("/llistarExemplars")
    public List<Exemplar> findAllExemplars() {
        return exemplarService.findAllExemplars();
    }
    
    @GetMapping("/llistarExemplarsLliures")
    public List<Exemplar> findAllExemplarsLliures() {
        return exemplarService.findExemplarsLliures();
    }
    
    @PutMapping("/actualitzarExemplar/{id}")
    public Exemplar updateExemplar(@PathVariable Long id,@RequestBody Exemplar exemplar) {
        return exemplarService.updateExemplar(id, exemplar);
    }
    
    @DeleteMapping("/eliminarExemplar/{id}")
    public String deleteExemplar(@PathVariable Long id) {
        exemplarService.deleteExemplar(id);
        return "Exemplar esborrat";
    }
    
    @GetMapping("/trobarExemplarPerId/{id}")
    Exemplar findExemplarById(@PathVariable Long id) {
        return exemplarService.findExemplarById(id);
    }
}
