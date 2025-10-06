/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dg
 */
@RestController
@RequestMapping("/api/greeting")
public class GreetingController {
    
    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();
    
    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello from API Biblioteca";
    }
}
