/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.AutorNotFoundException;
import com.bibliotecasedaos.biblioteca.error.UsuariNotFoundException;
import com.bibliotecasedaos.biblioteca.service.AutorService;
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
@RequestMapping("/biblioteca/autors")
@RestController
public class AutorController {
    
    @Autowired
    AutorService autorService;
    
    
    @GetMapping("/llistarAutors")
    public List<Autor> findAllAutors() {
        return autorService.findAllAutors();
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminarAutor/{id}")
    public String deleteAutor(@PathVariable Long id) throws AutorNotFoundException{
        autorService.deleteAutor(id);
        return "Autor esborrat";
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/afegirAutor")
    public Autor saveAutor(@RequestBody Autor autor) {
        return autorService.saveAutor(autor);
    }
}
