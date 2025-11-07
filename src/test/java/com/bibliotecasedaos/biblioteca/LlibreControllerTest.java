/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.config.SecurityConfig;
import com.bibliotecasedaos.biblioteca.controller.LlibreController;
import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.error.LlibreNotFoundException;
import com.bibliotecasedaos.biblioteca.service.LlibreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
//import static javax.swing.UIManager.get;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



/**
 * Proves unitaries per a la capa de controlador {@link LlibreController}.
 * 
 * @author David García Rodríguez
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    LlibreController.class, 
    TestSecurityConfig.class, 
})
@AutoConfigureMockMvc
@WebMvcTest(
    controllers = LlibreController.class,
    
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = org.springframework.stereotype.Service.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = org.springframework.stereotype.Component.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = org.springframework.stereotype.Repository.class)
    }
)
@Import(TestSecurityConfig.class)



public class LlibreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private LlibreService llibreService;

    private final String BASE_URL = "/biblioteca/llibres";

    /**
     * Prova la recuperació de tots els llibres.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser
    void findAllLlibres_shouldReturnListOfLlibres() throws Exception {

        Llibre llibre1 = new Llibre(); llibre1.setId(1L); llibre1.setTitol("Fahrenheit");
        Llibre llibre2 = new Llibre(); llibre2.setId(2L); llibre2.setTitol("1984");
        List<Llibre> llibres = Arrays.asList(llibre1, llibre2);

        when(llibreService.findAllLlibres()).thenReturn(llibres);

        mockMvc.perform(get(BASE_URL + "/llistarLlibres")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titol", is("Fahrenheit")));

        verify(llibreService, times(1)).findAllLlibres();
    }
    
    /**
     * Prova la cerca d'un llibre per ID.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció o el servei falla.
     */
    @Test
    @WithMockUser
    void findLlibreById_shouldReturnLlibre_whenFound() throws Exception {

        Llibre llibre = new Llibre(); llibre.setId(1L); llibre.setTitol("Test Book");
        when(llibreService.findLlibreById(1L)).thenReturn(llibre);

        mockMvc.perform(get(BASE_URL + "/trobarLlibrePerId/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titol", is("Test Book")));
        
        verify(llibreService, times(1)).findLlibreById(1L);
    }
      
    /**
     * Prova la creació d'un nou llibre per un usuari amb autoritat ADMIN.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció o la serialització falla.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void saveLlibre_shouldReturnLlibre_whenAdmin() throws Exception {

        Llibre llibreNou = new Llibre(); 
        llibreNou.setTitol("Nou Títol Válid");

        llibreNou.setIsbn("978-1234567890"); 
        llibreNou.setPagines(300);

        Autor autorMock = new Autor(1L, "Nom");
        llibreNou.setAutor(autorMock); 
        llibreNou.setEditorial("Editorial Válida");

        Llibre llibreDesat = new Llibre(); 
        llibreDesat.setId(1L); 
        llibreDesat.setTitol("Nou Títol Válid");
        llibreDesat.setIsbn("978-1234567890"); 
        llibreDesat.setPagines(300);
        llibreDesat.setAutor(autorMock);
        llibreDesat.setEditorial("Editorial Válida");

        when(llibreService.saveLlibre(any(Llibre.class))).thenReturn(llibreDesat);

        mockMvc.perform(put(BASE_URL + "/afegirLlibre").with(csrf())
                .content(objectMapper.writeValueAsString(llibreNou))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(llibreService, times(1)).saveLlibre(any(Llibre.class));
    }

    /**
     * Prova l'actualització d'un llibre existent per un usuari amb autoritat ADMIN.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateLlibre_shouldReturnUpdatedLlibre_whenAdmin2() throws Exception {
        Long id = 5L;

        Autor autorMock = new Autor(1L, "Nom");
    
        Llibre llibreEnviat = new Llibre(); 
        llibreEnviat.setId(id);
        llibreEnviat.setTitol("Títol Actualitzat");
        llibreEnviat.setIsbn("978-1234567890");
        llibreEnviat.setPagines(450);
        llibreEnviat.setEditorial("Ed. Prova");
        llibreEnviat.setAutor(autorMock);

        when(llibreService.updateLlibre(eq(id), any(Llibre.class))).thenReturn(llibreEnviat);

        mockMvc.perform(put(BASE_URL + "/actualitzarLlibre/" + id).with(csrf())
                .content(objectMapper.writeValueAsString(llibreEnviat))
                .contentType(MediaType.APPLICATION_JSON))
            
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titol", is("Títol Actualitzat")));

            verify(llibreService, times(1)).updateLlibre(eq(id), any(Llibre.class));
    }

    /**
     * Prova l'eliminació d'un llibre per ID per un usuari amb autoritat ADMIN.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void deleteLlibre_shouldReturnOk_whenAdmin() throws Exception {

        Long id = 5L;
        doNothing().when(llibreService).deleteLlibre(id);

        mockMvc.perform(delete(BASE_URL + "/eliminarLlibre/" + id).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Llibre esborrat"));

        verify(llibreService, times(1)).deleteLlibre(id);
    }
  
}
