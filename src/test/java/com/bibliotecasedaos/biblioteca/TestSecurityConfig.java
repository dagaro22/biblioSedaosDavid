/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import java.util.Arrays;

/**
 *
 * @author dg
 */  
@TestConfiguration // 👈 Aquesta anotació indica que és una classe de configuració per a tests
public class TestSecurityConfig {

    /**
     * Defineix un UserDetailsService per als tests.
     * @WebMvcTest el recollirà i permetrà que @WithMockUser funcioni.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        User adminUser = new User("admin", "password", 
                Arrays.asList(new SimpleGrantedAuthority("ADMIN"), 
                              new SimpleGrantedAuthority("USER")));
        User normalUser = new User("user", "password", 
                Arrays.asList(new SimpleGrantedAuthority("USER")));

        return new InMemoryUserDetailsManager(adminUser, normalUser);
    }
}