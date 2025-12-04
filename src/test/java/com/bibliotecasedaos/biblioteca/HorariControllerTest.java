/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.config.JwtService;
import com.bibliotecasedaos.biblioteca.config.TokenBlacklist;
import com.bibliotecasedaos.biblioteca.controller.HorariController;
import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.service.HorariService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
//import static org.springframework.boot.system.SystemProperties.get;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Proves unitaries per a la capa de controlador {@link HorariController}.
 * 
 * @author David García Rodríguez
 */
@WebMvcTest(HorariController.class)
public class HorariControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HorariService horariService;
    
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private TokenBlacklist tokenBlacklist;
    
    @MockBean 
    private UserDetailsService userDetailService;

    private Horari horariA;
    private Horari horariB;
    
    @BeforeEach
    void setUp() {
        
        horariA = new Horari();
        horariA.setId(1L);
        horariA.setSala("A");
        horariA.setDia("dilluns");
        horariA.setHora("12h");
        horariA.setEstat("lliure");
        
        horariB = new Horari();
        horariB.setId(2L);
        horariB.setSala("B");
        horariB.setDia("dimarts");
        horariB.setHora("14h");
        horariB.setEstat("lliure");
    }
    
    /**
     * Prova la recuperació de tots els horaris amb autoritat USER.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testListarTotsElsHoraris() throws Exception {
        
        List<Horari> horaris = Arrays.asList(horariA,horariB);
        when(horariService.findAllHoraris()).thenReturn(horaris);
        
        mockMvc.perform(get("/biblioteca/horaris/llistarHorarisSales"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].dia", is("dilluns")));
        
        verify(horariService, times(1)).findAllHoraris();
    }
    
    /**
     * Prova la creació d'un nou horari per un usuari amb autoritat ADMIN.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció o la serialització falla.
     */
    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testGuardarHorariAdminReturn200k() throws Exception {

        when(horariService.saveHorari(any(Horari.class))).thenReturn(horariA);

        mockMvc.perform(post("/biblioteca/horaris/afegirHorari")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(horariA)))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.dia", is("dilluns"))) 
                .andExpect(jsonPath("$.id", is(1))); 
                
        verify(horariService, times(1)).saveHorari(any(Horari.class));
    }
}
