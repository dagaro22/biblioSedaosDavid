/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.error.LlibreNotFoundException;
import com.bibliotecasedaos.biblioteca.service.LlibreService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/biblioteca/llibres")
@RestController
public class LlibreController {
    
    @Autowired
    LlibreService llibreService;
    
    @GetMapping("/llistarLlibres")
    public List<Llibre> findAllLlibres() {
        return llibreService.findAllLlibres();
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/afegirLlibre")
    public Llibre saveLlibre(@RequestBody Llibre llibre) {
        return llibreService.saveLlibre(llibre);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualitzarLlibre/{id}")
    public Llibre updateLlibre(@PathVariable Long id,@RequestBody Llibre llibre) throws LlibreNotFoundException {
        return llibreService.updateLlibre(id, llibre);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminarLlibre/{id}")
    public String deleteLlibre(@PathVariable Long id) throws LlibreNotFoundException {
        llibreService.deleteLlibre(id);
        return "Llibre esborrat";
    }
    
    @GetMapping("/trobarLlibrePerId/{id}")
    Llibre findLlibreById(@PathVariable Long id) throws LlibreNotFoundException {
        return llibreService.findLlibreById(id);
    }
}
