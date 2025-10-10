/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.controller.models.AuthenticationRequest;
import com.bibliotecasedaos.biblioteca.controller.models.RegisterRequest;
import com.bibliotecasedaos.biblioteca.entity.Role;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.repository.UsuariRepository;
import com.bibliotecasedaos.biblioteca.config.JwtService;
import com.bibliotecasedaos.biblioteca.controller.models.AuthResponse;
import com.bibliotecasedaos.biblioteca.service.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Proves unitàries per al servei d'autenticació (AuthServiceImpl).
 * Aïlla la lògica de negoci utilitzant Mockito per simular el comportament
 * de les dependències (Repository, PasswordEncoder, JwtService, AuthenticationManager).
 *
 * @author David García Rodríguez
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;
    
    //Creem els mocks per simular els objectes necessaris
    @Mock
    private UsuariRepository usuariRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private Usuari mockUsuari;
    private final String TEST_TOKEN = "mocked.jwt.token";
    private final String PASSWORD_USUARI = "PasswordUsuari";
    private final String PASSWORD_USUARI_ENCODED = "encodedPasswordUsuari";

    @BeforeEach
    void setUp() {
        // Configuració d'una sol·licitud de registre
        registerRequest = RegisterRequest.builder()
                .nick("nouUsuari")
                .password(PASSWORD_USUARI)
                .nom("Nou")
                .nif("11111111A")
                .rol(1)
                .build();

        // Configuració d'una sol·licitud d'autenticació
        authenticationRequest = AuthenticationRequest.builder()
                .nick("usuariExistent")
                .password(PASSWORD_USUARI)
                .build();

        // Usuari simulat tal com es recuperaria de la Base de dades
        mockUsuari = Usuari.builder()
                .id(10L)
                .nick("Pepito")
                .password(PASSWORD_USUARI_ENCODED)
                .nom("Pepe")
                .cognom1("Cognom1")
                .cognom2("Cognom2")
                .localitat("localitat")
                .provincia("Bcn")           
                .rol(2)
                .build();
    }

    /**
     * Verifica que el registre crea l'usuari correctament, el xifra, el desa
     * i retorna un token.
     */
    @Test
    void creteUsuariAndResponseToken() {

        when(passwordEncoder.encode(PASSWORD_USUARI)).thenReturn(PASSWORD_USUARI_ENCODED);

        when(usuariRepository.save(any(Usuari.class))).thenReturn(mockUsuari);

        when(jwtService.generateToken(any(Usuari.class))).thenReturn("token.fictici.prova");

        AuthResponse response = authService.register(registerRequest);

        verify(passwordEncoder, times(1)).encode(PASSWORD_USUARI);
        
        verify(usuariRepository, times(1)).save(any(Usuari.class));
        
        verify(jwtService, times(1)).generateToken(any(Usuari.class));

        assertThat(response.getToken()).isEqualTo("token.fictici.prova");
    }

    /**
     * Verifica que l'autenticació reeixida crida al gestor, recupera l'usuari
     * i retorna la resposta completa amb dades.
     */
    @Test
    void authenticateUsuariAndReturnFullAuthResponse() {

        when(usuariRepository.findUsuariByNickWithJPQL(authenticationRequest.getNick()))
                .thenReturn(Optional.of(mockUsuari));
        
        when(jwtService.generateToken(mockUsuari)).thenReturn(TEST_TOKEN);

        AuthResponse response = authService.authenticate(authenticationRequest);

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getNick(),
                        authenticationRequest.getPassword()
                )
        );

        verify(usuariRepository, times(1)).findUsuariByNickWithJPQL(authenticationRequest.getNick());
        
        //La resposta conté el token i les dades de l'usuari
        assertThat(response.getToken()).isEqualTo(TEST_TOKEN);
        assertThat(response.getId()).isEqualTo(mockUsuari.getId());
        assertThat(response.getNom()).isEqualTo(mockUsuari.getNom());

    }
}
