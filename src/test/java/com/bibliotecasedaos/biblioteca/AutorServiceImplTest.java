/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.error.AutorNotFoundException;
import com.bibliotecasedaos.biblioteca.repository.AutorRepository;
import com.bibliotecasedaos.biblioteca.service.AutorService;
import com.bibliotecasedaos.biblioteca.service.AutorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutorServiceImplTest {

    @InjectMocks
    private AutorServiceImpl autorService;
    
    @Mock
    private AutorRepository autorRepository;

    private Autor autor1;
    private Autor autor2;

    @BeforeEach
    void setUp() {
        autor1 = new Autor(1L, "García Márquez");
        autor2 = new Autor(2L, "Isabel Allende");
    }


    @Test
    void findAllAutors_shouldReturnListOfAutorsOrderedByName() {

        List<Autor> autors = Arrays.asList(autor1, autor2);
        
        when(autorRepository.findAllByOrderByNomAsc()).thenReturn(autors);

        List<Autor> result = autorService.findAllAutors();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(autorRepository, times(1)).findAllByOrderByNomAsc();
    }
    
    
    @Test
    void findAutorById_shouldReturnAutor() throws AutorNotFoundException {

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor1));

        Autor result = autorService.findAutorById(1L);

        assertNotNull(result);
        assertEquals("García Márquez", result.getNom());
        
        verify(autorRepository, times(1)).findById(1L);
    }
    
    @Test
    void saveAutor_shouldReturnSavedAutor() {

        when(autorRepository.save(autor1)).thenReturn(autor1);

        Autor result = autorService.saveAutor(autor1);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(autorRepository, times(1)).save(autor1);
    }
    
    @Test
    void deleteAutor_shouldDeleteAutor_whenIdExists() throws AutorNotFoundException {

        Long existingId = 1L;

        when(autorRepository.findById(existingId)).thenReturn(Optional.of(autor1));

        doNothing().when(autorRepository).deleteById(existingId);

        assertDoesNotThrow(() -> autorService.deleteAutor(existingId)); // Assegurem que no llança excepció

        verify(autorRepository, times(1)).findById(existingId);
        verify(autorRepository, times(1)).deleteById(existingId);
    }

    @Test
    void deleteAutor_shouldThrowException_whenIdDoesNotExist() {

        Long nonExistentId = 99L;

        when(autorRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(AutorNotFoundException.class, () -> {
            autorService.deleteAutor(nonExistentId);
        });

        verify(autorRepository, times(1)).findById(nonExistentId);
        verify(autorRepository, never()).deleteById(nonExistentId);
    }
    
}
