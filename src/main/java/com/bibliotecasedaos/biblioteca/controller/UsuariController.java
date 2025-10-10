/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.UsuariNotFoundException;
import com.bibliotecasedaos.biblioteca.service.UsuariService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dg
 */
@RequestMapping("/biblioteca/usuaris")
@RestController
public class UsuariController {
    
    @Autowired
    UsuariService usuariService;
    
    @GetMapping("/trobarUsuariPerId/{id}")
    Usuari findUsuariById(@PathVariable Long id) throws UsuariNotFoundException{
        return usuariService.findUsuariById(id);
    }
    
    @GetMapping("/trobarUsuariPerNick/{nick}")
    Optional<Usuari> findUsuariByNickWithJPQL(@PathVariable String nick) {
        return usuariService.findUsuariByNameWithJPQL(nick);
    }
    
    @GetMapping("/trobarUsuariPerNif/{nif}")
    Optional<Usuari> findUsuariByNifWithJPQL(@PathVariable String nif) {
        return usuariService.findByNif(nif);
    }
    
    @GetMapping("/trobarUsuariPerNifJ/{nif}")
    Optional<Usuari> findUsuariByNifWith(@PathVariable String nif) {
        return usuariService.findUsuariByNifWithJPQL(nif);
    }
    
    
    @GetMapping("/llistarUsuaris")
    public List<Usuari> findAllUsuaris() {
        return usuariService.findAllUsuaris();
    }
    
    
    @PutMapping("/actualitzarUsuari/{id}")
    public Usuari updateUsuari(@PathVariable Long id,@RequestBody Usuari usuari) {
        return usuariService.updateUsuari(id, usuari);
    }
    
    @DeleteMapping("/eliminarUsuari/{id}")
    public String deleteUsuari(@PathVariable Long id) {
        usuariService.deleteUsuari(id);
        return "Usuari esborrat";
    }
}
