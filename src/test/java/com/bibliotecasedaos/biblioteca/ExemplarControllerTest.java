/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.config.JwtFilter;
import com.bibliotecasedaos.biblioteca.config.JwtService;
import com.bibliotecasedaos.biblioteca.config.TokenBlacklist;
import com.bibliotecasedaos.biblioteca.controller.ExemplarController;
import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.error.ExemplarNotFoundException;
import com.bibliotecasedaos.biblioteca.service.ExemplarService;
import com.bibliotecasedaos.biblioteca.service.UsuariService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Proves unitàries per a la capa de controlador {@link ExemplarController}.
 * 
 * @author David García Rodríguez
 */
@WebMvcTest(ExemplarController.class)
public class ExemplarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExemplarService exemplarService;
    
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private TokenBlacklist tokenBlacklist;
    
    @MockBean 
    private UserDetailsService userDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    private Exemplar exemplar1;
    private final Long VALID_ID = 1L;
    private final Long INVALID_ID = 99L;

    private static final String BASE_URL = "/biblioteca/exemplars";

    @BeforeEach
    void setUp() {
        exemplar1 = new Exemplar();
        exemplar1.setId(VALID_ID);
        exemplar1.setLloc("E1-01");
        exemplar1.setReservat("N");
    }

    /**
     * Prova que un usuari autenticat pot llistar tots els exemplars.
     * Verifica l'estat 200 OK i el contingut de la llista retornada.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void findAllExemplars_shouldReturnListOfExemplars() throws Exception {
        // Arrange
        Exemplar exemplar2 = new Exemplar();
        exemplar2.setId(2L);
        List<Exemplar> allExemplars = Arrays.asList(exemplar1, exemplar2);

        when(exemplarService.findAllExemplars()).thenReturn(allExemplars);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/llistarExemplars")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].lloc", is("E1-01")));
        
        verify(exemplarService, times(1)).findAllExemplars();
    }
    
    /**
     * Prova la funcionalitat de cerca d'exemplars lliures filtrant per títol.
     * Verifica que es crida el servei amb el paràmetre {@code titol} i que l'autor és {@code null}.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void findExemplarsByTitolOrAutor_shouldCallServiceWithTitol() throws Exception {
        // Arrange
        String titol = "Java";
        when(exemplarService.findExemplarsLliuresByTitolOrAutor(eq(titol), isNull())).thenReturn(List.of(exemplar1));

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/llistarExemplarsLliures")
                .param("titol", titol)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
                
        verify(exemplarService, times(1)).findExemplarsLliuresByTitolOrAutor(eq(titol), isNull());
    }

    /**
     * Prova la cerca d'un exemplar per ID quan existeix.
     * Verifica l'estat 200 OK i les dades de l'exemplar.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */    
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"}) 
    void findExemplarById_shouldReturnExemplar_whenFound() throws Exception {
        when(exemplarService.findExemplarById(VALID_ID)).thenReturn(exemplar1);

        mockMvc.perform(get(BASE_URL + "/trobarExemplarPerId/{id}", VALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(VALID_ID.intValue())))
                .andExpect(jsonPath("$.lloc", is("E1-01")));

        verify(exemplarService, times(1)).findExemplarById(VALID_ID);
    }
    
    /**
     * Prova la cerca d'un exemplar per ID quan NO existeix.
     * Verifica que el controlador llança la resposta 404 Not Found.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"}) 
    void findExemplarById_shouldReturnNotFound_whenNotFound() throws Exception {

        when(exemplarService.findExemplarById(INVALID_ID)).thenThrow(new ExemplarNotFoundException("Exemplar no trobat"));

        mockMvc.perform(get(BASE_URL + "/trobarExemplarPerId/{id}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); 
                                                  
        verify(exemplarService, times(1)).findExemplarById(INVALID_ID);
    }

    /**
     * Prova la creació d'un nou exemplar amb rol ADMIN.
     * Verifica l'estat 200 OK i que es crida el servei de persistència.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveExemplar_asAdmin_shouldSucceed() throws Exception {
        Exemplar exemplarToSave = new Exemplar(null, "E3-05", "lliure", null);

        when(exemplarService.saveExemplar(any(Exemplar.class))).thenReturn(new Exemplar(1L, "E3-05", "lliure", null));

        mockMvc.perform(put("/biblioteca/exemplars/afegirExemplar")
            .content(objectMapper.writeValueAsString(exemplarToSave))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))          
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));

        verify(exemplarService, times(1)).saveExemplar(any(Exemplar.class));
    }      

    /**
     * Prova l'eliminació d'un exemplar per ID amb rol ADMIN.
     * Verifica que es crida el mètode d'eliminació del servei i es retorna 200 OK.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteExemplar_asAdmin_shouldSucceed() throws Exception {
        Long exemplarId = 1L;

        doNothing().when(exemplarService).deleteExemplar(exemplarId); 

        mockMvc.perform(delete("/biblioteca/exemplars/eliminarExemplar/{id}", exemplarId)
            .with(csrf()))                     
            .andExpect(status().isOk()); 

        verify(exemplarService, times(1)).deleteExemplar(exemplarId);
    }
    
    /**
     * Prova la restricció de seguretat per a l'eliminació d'exemplars sense rol ADMIN.
     * Verifica que es retorna l'estat 403 Forbidden i que el servei NO és cridat.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"USER"})
    void deleteExemplar_asUser_shouldBeForbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/eliminarExemplar/{id}", VALID_ID))
                .andExpect(status().isForbidden()); 

        verify(exemplarService, never()).deleteExemplar(anyLong());
    }
}
