/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.service.HorariService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author David García Rodríguez
 */
@RequestMapping("/biblioteca/horaris")
@RestController
public class HorariController {
    
    @Autowired
    HorariService horariService;
    
    @GetMapping("/llistarHorarisSales")
    public List<Horari> findAllHoraris() {
        return horariService.findAllHoraris();
    }
}
