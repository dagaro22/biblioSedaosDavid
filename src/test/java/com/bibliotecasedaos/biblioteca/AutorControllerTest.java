/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.config.JwtService;
import com.bibliotecasedaos.biblioteca.config.SecurityConfig;
import com.bibliotecasedaos.biblioteca.config.TokenBlacklist;
import com.bibliotecasedaos.biblioteca.controller.AutorController;
import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.error.AutorNotFoundException;
import com.bibliotecasedaos.biblioteca.service.AutorService;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dg
 */
@WebMvcTest(AutorController.class)
@Import(SecurityConfig.class)
public class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AutorService autorService;
    
    @MockBean
    private UserDetailsService userDetailsService;
    
    @MockBean 
    private JwtService jwtService; 

    @MockBean 
    private TokenBlacklist tokenBlacklist;
    
    @MockBean 
    private AuthenticationProvider authenticationProvider;

    private Autor autor1;
    private Autor autor2;

    @BeforeEach
    void setUp() {
        autor1 = new Autor(1L, "García Márquez");
        autor2 = new Autor(2L, "Isabel Allende");
    }
    
    public static String asJsonString(final Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @WithMockUser
    void findAllAutors_shouldReturnListOfAutors() throws Exception {
        List<Autor> allAutors = Arrays.asList(autor1, autor2);
        when(autorService.findAllAutors()).thenReturn(allAutors);

        mockMvc.perform(get("/biblioteca/autors/llistarAutors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) 
                .andExpect(jsonPath("$[0].nom", is(autor1.getNom())));

        verify(autorService, times(1)).findAllAutors();
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    void saveAutor_shouldSaveAndReturnAutor_whenUserIsAdmin() throws Exception {

        Autor newAutor = new Autor(null, "Julio Cortázar");
        Autor savedAutor = new Autor(3L, "Julio Cortázar");
        when(autorService.saveAutor(any(Autor.class))).thenReturn(savedAutor);

        mockMvc.perform(put("/biblioteca/autors/afegirAutor")
                .with(csrf())
                .content(asJsonString(newAutor))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperem 200 OK
                .andExpect(jsonPath("$.nom", is(savedAutor.getNom())));

        verify(autorService, times(1)).saveAutor(any(Autor.class));
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void saveAutor_shouldReturn403_whenUserIsNotAdmin() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/biblioteca/autors/afegirAutor")
                .with(csrf())
                .content(asJsonString(autor1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(autorService, never()).saveAutor(any(Autor.class));
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteAutor_shouldDeleteAndReturnOk_whenIdExistsAndUserIsAdmin() throws Exception {
        Long existingId = 1L;
        doNothing().when(autorService).deleteAutor(existingId);

        mockMvc.perform(delete("/biblioteca/autors/eliminarAutor/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Autor esborrat"));

        verify(autorService, times(1)).deleteAutor(existingId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteAutor_shouldReturn404_whenIdDoesNotExist() throws Exception {
        Long nonExistentId = 99L;
        doThrow(new AutorNotFoundException("Autor no trobat.")).when(autorService).deleteAutor(nonExistentId);

        mockMvc.perform(delete("/biblioteca/autors/eliminarAutor/99")
                .with(csrf()))
                .andExpect(status().isNotFound());
        
        verify(autorService, times(1)).deleteAutor(nonExistentId);
    }
    
}