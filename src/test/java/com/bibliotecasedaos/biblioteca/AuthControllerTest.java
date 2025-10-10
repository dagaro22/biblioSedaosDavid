/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.controller.AuthController;
import com.bibliotecasedaos.biblioteca.controller.models.AuthResponse;
import com.bibliotecasedaos.biblioteca.controller.models.AuthenticationRequest;
import com.bibliotecasedaos.biblioteca.controller.models.RegisterRequest;
import com.bibliotecasedaos.biblioteca.entity.Role;
import com.bibliotecasedaos.biblioteca.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prroves unitàries per a la capa de controlador AuthController.
 * Prova els endpoints de login i d'afegir usuari.
 *
 * @David García Rodríguez
 */
@SuppressWarnings("deprecation") 
@WebMvcTest(AuthController.class)
//Es deshabiliten els filtres de seguretat per provar endpoints públics.
@AutoConfigureMockMvc(addFilters = false) 
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //Mocks per simular els diferents beans necessaris
    @MockBean
    private AuthService authService; 
    @MockBean
    private com.bibliotecasedaos.biblioteca.config.JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private AuthenticationManager authenticationManager;

    private AuthResponse mockAuthResponse;

    /**
     * abans de començar els tests crea un objecte simulat que contindrà una resposta vàlida que conté un usuari
    */
    @BeforeEach
    void setUp() {
        mockAuthResponse = AuthResponse.builder()
                .token("tokendeprova")
                .id(1L)
                .nom("David")
                .role(Role.USER)
                .build();
    }

    /**
     * Prova que l'endpoint de registre maneja la sol·licitud correctament i torna HTTP 200.
     */
    @Test
    void testEsndpointAfegirUsuariReturnAuthResponseAndStatusOk() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .nick("Carli")
                .password("securePass")
                .nom("Carlos")
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(mockAuthResponse);

        mockMvc.perform(post("/biblioteca/auth/afegirUsuari")           
                .content(objectMapper.writeValueAsString(registerRequest))
                .contentType(MediaType.APPLICATION_JSON))
                // Verifica l'estat HTTP
                .andExpect(status().isOk())
                //Verifica el contingut de la resposta JSON
                .andExpect(jsonPath("$.token").value("tokendeprova"));
    }

    /**
     * Prova que l'endpoint de login maneja la sol·licitud i retorna HTTP 200 en autenticar-se.
     */
    @Test
    void testEsndpointLoginReturnAuthResponseAndStatusOk() throws Exception {
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .nick("existingUser")
                .password("correctPass")
                .build();

        when(authService.authenticate(any(AuthenticationRequest.class))).thenReturn(mockAuthResponse);

        mockMvc.perform(post("/biblioteca/auth/login")
                .content(objectMapper.writeValueAsString(authRequest))
                .contentType(MediaType.APPLICATION_JSON))
                // Verifica l'estat HTTP
                .andExpect(status().isOk())
                //Verifica el contingut de la resposta JSON
                .andExpect(jsonPath("$.token").value("tokendeprova"));
    }
}