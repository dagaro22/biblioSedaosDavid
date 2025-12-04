/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Grup;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
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
 * Controlador REST per a la gestió de grups, proporciona els endpoints necessaris.
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
    
    /**
     * Endpoint per obtenir una llista de tots els grups de reserva existents.
     * @return Una llista amb tots els objectes Grup.
     */
    @GetMapping("/llistarGrups")
    public List<Grup> findAllGrups() {
        return grupService.findAllGrups();
    }
    
    /**
     * Endpoint per crear un nou grup i reservar l'horari associat.
     * @param grup L'objecte Grup a crear. S'apliquen validacions mitjançant {@code @Valid}.
     * @return {@code ResponseEntity} amb el grup creat i l'estat HTTP 201 (CREATED).
     * @throws HorariNotFoundException Si l'horari especificat no existeix.
     * @throws HorariReservatException Si l'horari ja està ocupat.
     */
    @PostMapping("/afegirGrup")
    public ResponseEntity<Grup> saveGrup(@Valid @RequestBody Grup grup) throws HorariNotFoundException, HorariReservatException{
        Grup nouGrup = grupService.saveGrup(grup);
        return new ResponseEntity<>(nouGrup, HttpStatus.CREATED);
    }
    
    /**
     * Endpoint per eliminar un grup per ID, alliberant l'horari que tenia reservat.
     * L'usuari ha de tenir l'autoritat 'ADMIN' o l'usuari ha de ser l'administrador del grup.
     * @param id L'identificador del grup a eliminar.
     * @return Missatge de confirmació.
     * @throws GrupNotFoundException Si el grup no existeix.
     */
    @PreAuthorize("hasAuthority('ADMIN') or @grupSecurity.esAdminDelGrup(#id, authentication.principal.id)")
    @DeleteMapping("/eliminarGrup/{id}")
    public String deleteGrup(@PathVariable Long id) throws GrupNotFoundException {
        grupService.deleteGrup(id);
        return "Grup esborrat";
    }
    
    /**
     * Endpoint per afegir un usuari com a membre d'un grup.
     * L'usuari que fa la petició ha de ser el mateix usuari que s'està afegint.
     * @param grupId L'identificador del grup.
     * @param membreId L'identificador de l'usuari que es vol afegir.
     * @return {@code ResponseEntity} amb el grup actualitzat i l'estat HTTP 200 (OK).
     * @throws GrupNotFoundException Si el grup no existeix.
     * @throws UsuariNotFoundException Si l'usuari no existeix.
     * @throws LimitDeMembresSuperatException Si s'excedeix el màxim de membres.
     * @throws MembreJaExisteixException Si l'usuari ja és membre del grup.
     */
    @PreAuthorize("#membreId == authentication.principal.id")
    @PutMapping("/{grupId}/afegirUsuariGrup/{membreId}")
    public ResponseEntity<Grup> afegirMembre(@PathVariable Long grupId, @PathVariable Long membreId) 
            throws GrupNotFoundException, UsuariNotFoundException, LimitDeMembresSuperatException, MembreJaExisteixException {
        
        Grup grupActualitzat = grupService.afegirUsuariGrup(grupId, membreId);
        
        return ResponseEntity.ok(grupActualitzat);
    }
    
    /**
     * Endpoint per llistar tots els usuaris que són membres d'un grup específic.
     * @param grupId L'identificador del grup.
     * @return {@code ResponseEntity} amb la llista d'usuaris i l'estat HTTP 200 (OK).
     * @throws GrupNotFoundException Si el grup no existeix.
     */
    @GetMapping("/llistarUsuarisGrup/{grupId}")
    public ResponseEntity<List<Usuari>> llistarMembres(@PathVariable Long grupId) throws GrupNotFoundException {
        
        List<Usuari> membres = grupService.trobarMemebresGrup(grupId);  
        return ResponseEntity.ok(membres);
    }
    
    /**
     * Endpoint per eliminar un usuari de la llista de membres d'un grup (sortir del grup).
     * L'usuari ha de tenir l'autoritat 'ADMIN' o l'usuari que fa la petició ha de ser el mateix usuari que s'està eliminant.
     * @param grupId L'identificador del grup.
     * @param membreId L'identificador de l'usuari que se'n va del grup.
     * @return Missatge de confirmació.
     * @throws GrupNotFoundException Si el grup no existeix.
     * @throws UsuariNotFoundException Si l'usuari no es troba dins del grup.
     */
    @PreAuthorize("hasAuthority('ADMIN') or #membreId == authentication.principal.id")
    @DeleteMapping("/{grupId}/sortirUsuari/{membreId}")
    public String eliminarUsuariDeGrup(@PathVariable Long grupId, @PathVariable Long membreId) throws GrupNotFoundException, UsuariNotFoundException {
        grupService.eliminarUsuariDeGrup(grupId, membreId);
        return "Usuari amb id " + membreId + " esborrat del grup amb id " +grupId;
    }
}
