/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.service.HorariService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controlador REST per a la gestió d'horaris, proporciona els endpoints necessaris.
 * 
 * @author David García Rodríguez
 */
@RequestMapping("/biblioteca/horaris")
@RestController
public class HorariController {
    
    @Autowired
    HorariService horariService;
    
    /**
     * Endpoint per llistar tots els horaris disponibles.
     * @return Una llista amb tots els objectes Horari recuperats del servei.
     */
    @GetMapping("/llistarHorarisSales")
    public List<Horari> findAllHoraris() {
        return horariService.findAllHoraris();
    }
    
    /**
     * Endpoint per afegir un nou horari o actualitzar un existent.
     * Requereix que l'usuari estigui autenticat i tingui el permís 'ADMIN'.
     * @param horari L'objecte Horari a desar.
     * @return L'objecte Horari que ha estat desat i persistit.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/afegirHorari")
    public Horari saveHorari(@Valid @RequestBody Horari horari) {
        return horariService.saveHorari(horari);
    }
}
