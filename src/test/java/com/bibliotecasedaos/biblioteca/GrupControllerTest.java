/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.config.JwtService;
import com.bibliotecasedaos.biblioteca.config.TokenBlacklist;
import com.bibliotecasedaos.biblioteca.controller.GrupController;
import com.bibliotecasedaos.biblioteca.entity.Grup;
import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.HorariReservatException;
import com.bibliotecasedaos.biblioteca.security.GrupSecurity;
import com.bibliotecasedaos.biblioteca.service.GrupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


/**
 * Proves unitaries per a la capa de controlador {@link GrupController}.
 * 
 * @author David García Rodríguez
 */
@WebMvcTest(GrupController.class)
public class GrupControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private GrupService grupService;
    
    @MockBean
    private GrupSecurity grupSecurity;
    
    @MockBean
    private JwtService jwtService;

    @MockBean
    private TokenBlacklist tokenBlackList;
    
    @MockBean
    private UserDetailsService userDetailsService;

    
    private Grup grup;
    private Usuari admin;
    private Usuari membre1;
    private Horari horari;
    
    @BeforeEach
    void setUp() {
                
        admin = Usuari.builder().id(1L).nom("Admin User").build();
        membre1 = Usuari.builder().id(2L).nom("Normal User").build();
        horari = Horari.builder().id(10L).estat("lliure").build();
        
        grup = Grup.builder()
                .id(100L)
                .nom("Grup Llibres")
                .horari(horari)
                .administrador(admin)
                .membres(new ArrayList<>(List.of(admin, membre1)))
                .build();
    }
    
    /**
     * Prova l'endpoint per llistar tots els grups de lectura.
     * @throws Exception
     */
    @Test
    @WithMockUser
    void testLlistarGrupsDeLectura() throws Exception {

        List<Grup> grupsLlista = List.of(grup);
        when(grupService.findAllGrups()).thenReturn(grupsLlista);

        mockMvc.perform(get("/biblioteca/grups/llistarGrups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Grup Llibres"))
                .andExpect(jsonPath("$.length()").value(1));

        verify(grupService, times(1)).findAllGrups();
    }
    
    /**
     * Prova l'endpoint per afegir un nou grup de lectura.
     * @throws Exception
     */
    @Test
    @WithMockUser
    void testAfegirGrup() throws Exception {

        Grup grupNou = Grup.builder().nom("Grup Nou").horari(horari).build();
        Grup grupGuardat = Grup.builder().id(101L).nom("Grup Nou").horari(horari).build();

        when(grupService.saveGrup(any(Grup.class))).thenReturn(grupGuardat);

        mockMvc.perform(post( "/biblioteca/grups/afegirGrup")
                .with(csrf()) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(grupNou)))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.nom").value("Grup Nou"));

        verify(grupService, times(1)).saveGrup(any(Grup.class));
    }
    
    /**
     * Prova la gestió d'excepcions de negoci durant la creació d'un grup.
     * @throws HorariReservatException
     */
    @Test
    @WithMockUser
    void testAfegirGrup_HorariReservatException() throws Exception {

        Grup grupNou = Grup.builder().nom("Grup Problema").horari(horari).build();
        when(grupService.saveGrup(any(Grup.class)))
                .thenThrow(new HorariReservatException("Horari ja reservat."));

        mockMvc.perform(post("/biblioteca/grups/afegirGrup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(grupNou)))
                .andExpect(status().isConflict()); 
        
        verify(grupService, times(1)).saveGrup(any(Grup.class));
    }
    
    /**
     * Prova l'endpoint per eliminar un grup (requereix rol ADMIN).
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"}) 
    void testEliminarGrup() throws Exception {

        doNothing().when(grupService).deleteGrup(100L);

        mockMvc.perform(delete("/biblioteca/grups/eliminarGrup/{id}", 100L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Grup esborrat"));

        verify(grupService, times(1)).deleteGrup(100L);
    }
    
    /**
     * Prova l'endpoint per afegir un membre a un grup existent.
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "3", authorities = {"USER"}) 
    void testAfegirMembreAUnGrup() throws Exception {

        Usuari nouMembre = Usuari.builder().id(3L).nom("Nou Membre").build();
        Grup grupActualitzat = Grup.builder().id(100L).membres(List.of(admin, membre1, nouMembre)).build();

        when(grupService.afegirUsuariGrup(100L, 3L)).thenReturn(grupActualitzat);

        mockMvc.perform(put("/biblioteca/grups/{grupId}/afegirUsuariGrup/{membreId}", 100L, 3L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.membres.length()").value(3));

        verify(grupService, times(1)).afegirUsuariGrup(100L, 3L);
    }
    
    /**
     * Prova l'endpoint per llistar els membres d'un grup específic.
     * @throws Exception
     */
    @Test
    @WithMockUser
    void testLlistarMembresDeGrup() throws Exception {

        when(grupService.trobarMemebresGrup(100L)).thenReturn(grup.getMembres());

        mockMvc.perform(get("/biblioteca/grups/llistarUsuarisGrup/{grupId}", 100L)               
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].nom").value("Normal User"));

        verify(grupService, times(1)).trobarMemebresGrup(100L);
    }
    
    /**
     * Prova l'endpoint per eliminar un usuari (membre) d'un grup.
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"}) 
    void testEliminarUsuariDeGrup() throws Exception {

        doNothing().when(grupService).eliminarUsuariDeGrup(100L, 2L);

        mockMvc.perform(delete("/biblioteca/grups/{grupId}/sortirUsuari/{membreId}", 100L, 2L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuari amb id 2 esborrat del grup amb id 100"));

        verify(grupService, times(1)).eliminarUsuariDeGrup(100L, 2L);
    }
}
