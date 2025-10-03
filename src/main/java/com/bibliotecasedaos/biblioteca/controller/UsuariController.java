/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.service.UsuariService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dg
 */
@RestController
public class UsuariController {
    
    @Autowired
    UsuariService usuariService;
    
    @GetMapping("/trobarUsuariPerNick/{nick}")
    Optional<Usuari> findUsuariByNickWithJPQL(@PathVariable String nick) {
        return usuariService.findUsuariByNameWithJPQL(nick);
    }
    
    @GetMapping("/trobarUsuariPerNif/{nif}")
    Optional<Usuari> findUsuariByNifWithJPQL(@PathVariable String nif) {
        return usuariService.findByNif(nif);
    }
    
    @GetMapping("/llistarUsuaris")
    public List<Usuari> findAllUsuaris() {
        return usuariService.findAllUsuaris();
    }
    
    @PostMapping("/afegirUsuari")
    public Usuari saveLocal(@RequestBody Usuari usuari) {
        return usuariService.saveUsuari(usuari);
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
