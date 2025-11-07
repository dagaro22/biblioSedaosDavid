/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.config.JwtService;
import com.bibliotecasedaos.biblioteca.config.TokenBlacklist;
import com.bibliotecasedaos.biblioteca.controller.UsuariController;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.service.AutorService;
import com.bibliotecasedaos.biblioteca.service.UsuariService;
import com.bibliotecasedaos.biblioteca.service.UsuariServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import static org.h2.store.fs.FileUtils.delete;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.boot.system.SystemProperties.get;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.util.Arrays;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong; 
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



/**
 *
 * @author David García Rodríguez
 */
@WebMvcTest(UsuariController.class)
@Import(TestSecurityConfig.class)
public class UsuariControllerTest {
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean 
    private UsuariService usuariService;  

    private final String BASE_URL = "/biblioteca/usuaris";

    private final Long USER_ID = 5L;
    private final String ADMIN_NICK = "adminUser";
    private final String USER_NICK = "regularUser";
    private final String USER_NIF = "12345678A";

    private Usuari buildUsuari(Long id, String nick, String nif) {
        Usuari u = new Usuari();
        u.setId(id);
        u.setNick(nick);
        u.setNif(nif);
        
        return u;
    }

    /**
     * Prova la recuperació d'un usuari per ID per un usuari amb rol ADMIN.
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void findUsuariById_shouldReturnUsuari_whenAdmin() throws Exception {

        Usuari usuari = buildUsuari(USER_ID, USER_NICK, USER_NIF);
        when(usuariService.findUsuariById(USER_ID)).thenReturn(usuari);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/trobarUsuariPerId/" + USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick", is(USER_NICK)));

        verify(usuariService, times(1)).findUsuariById(USER_ID);
    }
    
    /**
     * Prova la recuperació d'un usuari per ID quan l'usuari autenticat és el propietari del recurs.
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(username = "owner", authorities = {"USER"})
    void findUsuariById_shouldReturnUsuari_whenResourceOwner() throws Exception {

        Usuari usuari = buildUsuari(USER_ID, "owner", USER_NIF);

        when(usuariService.isResourceOwner(eq(USER_ID), any(Authentication.class))).thenReturn(true); 
        when(usuariService.findUsuariById(USER_ID)).thenReturn(usuari);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/trobarUsuariPerId/" + USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick", is("owner")));

        verify(usuariService, times(1)).findUsuariById(USER_ID);
    }


    /**
     * Prova la recuperació d'un usuari per Nick per un usuari amb rol ADMIN.
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void findUsuariByNick_shouldReturnUsuari_whenAdmin() throws Exception {

        Usuari usuari = buildUsuari(USER_ID, USER_NICK, USER_NIF);
        when(usuariService.findUsuariByNameWithJPQL(USER_NICK)).thenReturn(Optional.of(usuari));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/trobarUsuariPerNick/" + USER_NICK)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick", is(USER_NICK)));

        verify(usuariService, times(1)).findUsuariByNameWithJPQL(USER_NICK);
    }

    /**
     * Prova la recuperació de tots els usuaris per un usuari amb rol ADMIN.
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void findAllUsuaris_shouldReturnListOfUsuaris_whenAdmin() throws Exception {

        List<Usuari> usuaris = Arrays.asList(
            buildUsuari(1L, "u1", "11111111A"),
            buildUsuari(2L, "u2", "22222222B")
        );
        when(usuariService.findAllUsuaris()).thenReturn(usuaris);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/llistarUsuaris")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nick", is("u1")));

        verify(usuariService, times(1)).findAllUsuaris();
    }


    /**
     * Prova l'eliminació d'un usuari per un usuari autenticat (assumint que la lògica de seguretat en el controlador permet
     * l'acció a usuaris autenticats).
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    @WithMockUser
    void deleteUsuari_shouldReturnOk_whenAuthenticated() throws Exception {

        doNothing().when(usuariService).deleteUsuari(USER_ID);

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/eliminarUsuari/" + USER_ID).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuari esborrat"));

        verify(usuariService, times(1)).deleteUsuari(USER_ID);
    }
    
    /**
     * Prova l'intent d'eliminació sense autenticació.
     * Verifica que es retorna l'estat 401 Unauthorized i que el servei no és cridat.
     *
     * @throws Exception Si {@link MockMvc} llança una excepció.
     */
    @Test
    void deleteUsuari_shouldReturnUnauthorized_whenUnauthenticated() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/eliminarUsuari/" + USER_ID).with(csrf()))
                .andExpect(status().isUnauthorized());

        verify(usuariService, never()).deleteUsuari(anyLong());
    }
}
