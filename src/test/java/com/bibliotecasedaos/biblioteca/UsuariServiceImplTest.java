/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.UsuariNotFoundException;
import com.bibliotecasedaos.biblioteca.repository.UsuariRepository;
import com.bibliotecasedaos.biblioteca.service.UsuariServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Proves unitàries per a la implementació del servei {@link UsuariServiceImpl}
 * 
 * @author David García Rodríguez
 */
@ExtendWith(MockitoExtension.class)
public class UsuariServiceImplTest {

    @InjectMocks
    private UsuariServiceImpl usuariService;

    @Mock
    private UsuariRepository usuariRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuari usuari1;
    private Usuari usuari2;

    @BeforeEach
    void setUp() {
        usuari1 = new Usuari(1L, "admin", "1234", "Admin", "Cognom1", "Cognom2", "12345678A", "admin@biblioteca.com", "Carrer A", "Localitat A", "CP A", "Província A", "600000000", 1);
        usuari2 = new Usuari(2L, "usuari", "5678", "User", "CognomX", "CognomY", "87654321B", "user@biblioteca.com", "Carrer B", "Localitat B", "CP B", "Província B", "611111111", 0);
    }

    /**
     * Prova la recuperació de tots els usuaris.
     */
    @Test
    void findAllUsuaris_shouldReturnAllUsers() {

        List<Usuari> usuaris = Arrays.asList(usuari1, usuari2);
        when(usuariRepository.findAll()).thenReturn(usuaris);

        List<Usuari> found = usuariService.findAllUsuaris();

        assertNotNull(found);
        assertEquals(2, found.size());
        verify(usuariRepository, times(1)).findAll();
    }

    /**
     * Prova el guardat d'un nou usuari.
     */
    @Test
    void saveUsuari_shouldReturnSavedUser() {

        Usuari usuariNou = new Usuari(null, "nou", "pass", "Nou", null, null, null, null, null, null, null, null, null, 0);
        when(usuariRepository.save(usuariNou)).thenReturn(usuari1);

        Usuari saved = usuariService.saveUsuari(usuariNou);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(usuariRepository, times(1)).save(usuariNou);
    }

    /**
     * Prova la cerca d'un usuari per ID existent.
     *
     * @throws UsuariNotFoundException Si l'usuari no és trobat.
     */
    @Test
    void findUsuariById_shouldReturnUser_whenFound() throws UsuariNotFoundException {

        when(usuariRepository.findById(1L)).thenReturn(Optional.of(usuari1));

        Usuari found = usuariService.findUsuariById(1L);

        assertNotNull(found);
        assertEquals("admin", found.getNick());
        verify(usuariRepository, times(1)).findById(1L);
    }

    /**
     * Prova l'actualització reeixida de dades d'usuari, incloent-hi la contrasenya.
     * 
     * @throws UsuariNotFoundException Si l'usuari no és trobat.
     */
    @Test
    void updateUsuari_shouldUpdateAllFieldsIncludingPassword() throws UsuariNotFoundException {

        Long id = 1L;
        String newPass = "nouSecret";
        String encodedPass = "encodedNouSecret";

        Usuari updateData = new Usuari();
        updateData.setPassword(newPass);
        updateData.setNom("Nom Actualitzat");
        updateData.setEmail("nou.email@test.com");
        
        when(usuariRepository.findById(id)).thenReturn(Optional.of(usuari1));
        when(passwordEncoder.encode(newPass)).thenReturn(encodedPass);
        when(usuariRepository.save(any(Usuari.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuari updated = usuariService.updateUsuari(id, updateData);

        assertEquals("Nom Actualitzat", updated.getNom());
        assertEquals("nou.email@test.com", updated.getEmail());
        assertEquals(encodedPass, updated.getPassword());
        assertEquals(usuari1.getNick(), updated.getNick()); 

        verify(usuariRepository, times(1)).findById(id);
        verify(passwordEncoder, times(1)).encode(newPass);
        verify(usuariRepository, times(1)).save(updated);
    }
    
    /**
     * Prova l'eliminació d'un usuari.
     */
    @Test
    void deleteUsuari_shouldCallRepositoryDelete() {

        Long id = 2L;

        doNothing().when(usuariRepository).deleteById(id);

        usuariService.deleteUsuari(id);

        verify(usuariRepository, times(1)).deleteById(id);
    }
    
    /**
     * Prova la cerca d'un usuari per nom d'usuari mitjançant el mètode amb JPQL.
     *
     * @throws UsuariNotFoundException Si l'usuari no és trobat.
     */
    @Test
    void findUsuariByNameWithJPQL_shouldReturnUser_whenFound() throws UsuariNotFoundException {

        when(usuariRepository.findUsuariByNickWithJPQL("admin")).thenReturn(Optional.of(usuari1));

        Optional<Usuari> found = usuariService.findUsuariByNameWithJPQL("admin");

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        verify(usuariRepository, times(1)).findUsuariByNickWithJPQL("admin");
    }

    /**
     * Prova que la cerca per nom d'usuari amb JPQL llança una excepció quan no es troba.
     */
    @Test
    void findUsuariByNameWithJPQL_shouldThrowException_whenNotFound() {

        when(usuariRepository.findUsuariByNickWithJPQL("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsuariNotFoundException.class, () -> usuariService.findUsuariByNameWithJPQL("nonexistent"));
        verify(usuariRepository, times(1)).findUsuariByNickWithJPQL("nonexistent");
    }

}