/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Grup;
import com.bibliotecasedaos.biblioteca.error.GrupNotFoundException;
import com.bibliotecasedaos.biblioteca.error.HorariNotFoundException;
import com.bibliotecasedaos.biblioteca.error.HorariReservatException;
import com.bibliotecasedaos.biblioteca.error.LimitDeMembresSuperatException;
import com.bibliotecasedaos.biblioteca.error.MembreJaExisteixException;
import com.bibliotecasedaos.biblioteca.error.UsuariNotFoundException;
import com.bibliotecasedaos.biblioteca.service.GrupService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author David García Rodríguez
 */
@RequestMapping("/biblioteca/grups")
@RestController
public class GrupController {
    
    private final GrupService grupService;
    
    @Autowired
    public GrupController(GrupService grupService) {
        this.grupService = grupService;
    }
    
    @GetMapping("/llistarGrups")
    public List<Grup> findAllGrups() {
        return grupService.findAllGrups();
    }
    
    @PostMapping("/afegirGrup")
    public ResponseEntity<Grup> saveGrup(@Valid @RequestBody Grup grup) throws HorariNotFoundException, HorariReservatException{
        Grup nouGrup = grupService.saveGrup(grup);
        return new ResponseEntity<>(nouGrup, HttpStatus.CREATED);
    }
    
    @PreAuthorize("hasAuthority('ADMIN') or @grupSecurity.esAdminDelGrup(#id, authentication.principal.id)")
    @DeleteMapping("/eliminarGrup/{id}")
    public String deleteGrup(@PathVariable Long id) throws GrupNotFoundException {
        grupService.deleteGrup(id);
        return "Grup esborrat";
    }
    
    @PreAuthorize("#membreId == authentication.principal.id")
    @PutMapping("/{grupId}/afegirUsuariGrup/{membreId}")
    public ResponseEntity<Grup> afegirMembre(@PathVariable Long grupId, @PathVariable Long membreId) 
            throws GrupNotFoundException, UsuariNotFoundException, LimitDeMembresSuperatException, MembreJaExisteixException {
        
        Grup grupActualitzat = grupService.afegirUsuariGrup(grupId, membreId);
        
        return ResponseEntity.ok(grupActualitzat);
    }
}
