/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.config.JwtService;
import com.bibliotecasedaos.biblioteca.config.TokenBlacklist;
import com.bibliotecasedaos.biblioteca.controller.PrestecController;
import com.bibliotecasedaos.biblioteca.entity.Prestec;
import com.bibliotecasedaos.biblioteca.service.PrestecService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import org.springframework.security.access.prepost.PreAuthorize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

/**
 * Proves unitaries per a la capa de controlador {@link PrestecController}.
 * @author David García Rodríguez
 */
@WebMvcTest(
    controllers = PrestecController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*SecurityConfig.*") 
)
public class PrestecControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrestecService prestecService;
    
    @MockBean // 💡 NECESSARI
    private JwtService jwtService;
    
    @MockBean
    private TokenBlacklist tokenBlacklist;

    private CustomPrincipal userPrincipal1;
    private CustomPrincipal userPrincipal2;

    private static final String BASE_URL = "/biblioteca/prestecs";

    private Usuari usuari1;
    private Exemplar exemplar1;
    
    private Prestec prestec1;
    private Prestec prestec2;
    private final Long USER_ID_1 = 1L;
    private final Long USER_ID_2 = 2L;

    @BeforeEach
    void setUp() {

        usuari1 = new Usuari();
        usuari1.setId(USER_ID_1);
        exemplar1 = new Exemplar();
        exemplar1.setId(10L);

        userPrincipal1 = new CustomPrincipal(USER_ID_1, "user1", "ROLE_USER");
        userPrincipal2 = new CustomPrincipal(USER_ID_2, "user2", "ROLE_USER");

        prestec1 = new Prestec(1L, LocalDate.now(), null, usuari1, exemplar1);

        Usuari usuari2 = new Usuari();
        usuari2.setId(USER_ID_2);
        prestec2 = new Prestec(2L, LocalDate.now().minusDays(5), null, usuari2, exemplar1);
    }

    private static class CustomPrincipal extends org.springframework.security.authentication.UsernamePasswordAuthenticationToken {
        private final Long id;

        public CustomPrincipal(Long id, String name, String role) {
            super(name, "password", Collections.singletonList(new SimpleGrantedAuthority(role)));
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    /**
     * Prova la cerca de préstecs actius per un usuari amb autoritat ADMIN.
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void findPrestecsActius_asAdmin_shouldReturnAllActivePrestecs() throws Exception {

        when(prestecService.findPrestecsActius(isNull())).thenReturn(List.of(prestec1, prestec2));

        mockMvc.perform(get(BASE_URL + "/llistarPrestecsActius")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(prestecService, times(1)).findPrestecsActius(isNull());
    }

    /**
     * Prova la cerca de préstecs actius per l'usuari propietari.
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    void findPrestecsActius_asOwner_shouldReturnOwnActivePrestecs() throws Exception {

        when(prestecService.findPrestecsActius(eq(USER_ID_1))).thenReturn(List.of(prestec1));

        mockMvc.perform(get(BASE_URL + "/llistarPrestecsActius")
                .param("usuariId", USER_ID_1.toString())
                .with(authentication(userPrincipal1)) 
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(prestecService, times(1)).findPrestecsActius(eq(USER_ID_1));
    }


    /**
     * Prova la cerca de tots els préstecs (actius i finalitzats) per un usuari ADMIN.
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void findAllPrestecs_asAdmin_shouldReturnAllPrestecs() throws Exception {

        when(prestecService.findAllPrestecs(isNull())).thenReturn(List.of(prestec1, prestec2));

        mockMvc.perform(get(BASE_URL + "/llistarPrestecs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(prestecService, times(1)).findAllPrestecs(isNull());
    }

    /**
     * Prova la creació d'un nou préstec per un usuari amb autoritat ADMIN.
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void savePrestec_asAdmin_shouldReturnCreatedPrestec() throws Exception {

        Prestec prestecNou = new Prestec(null, LocalDate.now(), null, usuari1, exemplar1);
        Prestec prestecDesat = new Prestec(5L, LocalDate.now(), null, usuari1, exemplar1);

        when(prestecService.savePrestec(any(Prestec.class))).thenReturn(prestecDesat);

        mockMvc.perform(post(BASE_URL + "/afegirPrestec")
                .with(csrf()) 
                .content(objectMapper.writeValueAsString(prestecNou))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.id", is(5)));

        verify(prestecService, times(1)).savePrestec(any(Prestec.class));
    }


    /**
     * Prova la devolució d'un préstec per un usuari amb autoritat ADMIN.
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void retornarPrestec_asAdmin_shouldSucceed() throws Exception {
        // Arrange
        Long prestecId = 1L;
        doNothing().when(prestecService).retornarPrestec(prestecId);

        mockMvc.perform(put(BASE_URL + "/ferDevolucio/{prestecId}", prestecId)
                .with(csrf())) 
                .andExpect(status().isOk());

        verify(prestecService, times(1)).retornarPrestec(prestecId);
    }
}