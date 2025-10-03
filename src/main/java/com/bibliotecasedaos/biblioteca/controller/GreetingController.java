/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.controller;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dg
 */
@RestController
public class GreetingController {
    
    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();
    
    @GetMapping("/greeting")
    public Usuari helloWorld(@RequestParam(value = "name", defaultValue = "World") String name) {
        //return new Usuari(counter.incrementAndGet(), String.format(template, name));
        return null;
    }
}
