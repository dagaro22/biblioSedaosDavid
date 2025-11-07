/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.error.ExemplarNotFoundException;
import com.bibliotecasedaos.biblioteca.repository.ExemplarRepository;
import com.bibliotecasedaos.biblioteca.service.ExemplarServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Proves unitàries per a la implementació del servei {@link ExemplarServiceImpl}.
 * 
 * @author David García Rodríguez
 */
@ExtendWith(MockitoExtension.class)
public class ExemplarServiceImplTest {

    @Mock
    private ExemplarRepository exemplarRepository;

    @InjectMocks
    private ExemplarServiceImpl exemplarService;

    private Exemplar exemplar1;
    private Exemplar exemplar2;

    @BeforeEach
    void setUp() {

        exemplar1 = new Exemplar();
        exemplar1.setId(1L);
        exemplar1.setLloc("A1");
        exemplar1.setReservat("lliure");

        exemplar2 = new Exemplar();
        exemplar2.setId(2L);
        exemplar2.setLloc("B2");
        exemplar2.setReservat("reservat");
    }

    /**
     * Prova la recuperació de tots els exemplars.
     */
    @Test
    void findAllExemplars_shouldReturnAllExemplars() {
        
        List<Exemplar> expectedExemplars = Arrays.asList(exemplar1, exemplar2);
        when(exemplarRepository.findAll()).thenReturn(expectedExemplars);

        List<Exemplar> actualExemplars = exemplarService.findAllExemplars();

        assertEquals(2, actualExemplars.size());
        verify(exemplarRepository, times(1)).findAll();
    }

    /**
     * Prova el guardat d'un nou exemplar.
     */
    @Test
    void saveExemplar_shouldReturnSavedExemplar() {

        when(exemplarRepository.save(exemplar1)).thenReturn(exemplar1);

        Exemplar savedExemplar = exemplarService.saveExemplar(exemplar1);

        assertNotNull(savedExemplar);
        assertEquals("A1", savedExemplar.getLloc());
        verify(exemplarRepository, times(1)).save(exemplar1);
    }

    /**
     * Prova la cerca d'un exemplar per ID existent.
     *
     * @throws ExemplarNotFoundException Si l'exemplar no és trobat (no hauria d'ocórrer).
     */
    @Test
    void findExemplarById_shouldReturnExemplar_whenFound() throws ExemplarNotFoundException {

        when(exemplarRepository.findById(1L)).thenReturn(Optional.of(exemplar1));

        Exemplar foundExemplar = exemplarService.findExemplarById(1L);

        assertNotNull(foundExemplar);
        assertEquals(1L, foundExemplar.getId());
    }

    /**
     * Prova el maneig de l'excepció quan l'ID no és trobat.
     * Verifica que es llança {@link ExemplarNotFoundException}.
     */
    @Test
    void findExemplarById_shouldThrowException_whenNotFound() {
        // Arrange
        when(exemplarRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExemplarNotFoundException.class, () -> {
            exemplarService.findExemplarById(99L);
        }, "Exemplar amb ID 99 no trobat.");
    }
    

    /**
     * Prova l'actualització reeixida de tots els camps d'un exemplar existent.
     *
     * @throws ExemplarNotFoundException Si l'exemplar no és trobat.
     */
    @Test
    void updateExemplar_shouldUpdateAllFields_whenValid() throws ExemplarNotFoundException {

        Exemplar updateData = new Exemplar();
        updateData.setLloc("C3");
        updateData.setReservat("prestat");
        
        when(exemplarRepository.findById(1L)).thenReturn(Optional.of(exemplar1));
        when(exemplarRepository.save(exemplar1)).thenReturn(exemplar1);

        Exemplar updatedExemplar = exemplarService.updateExemplar(1L, updateData);

        assertEquals("C3", updatedExemplar.getLloc());
        assertEquals("prestat", updatedExemplar.getReservat());
        verify(exemplarRepository, times(1)).save(exemplar1);
    }
    
    /**
     * Prova l'actualització quan l'exemplar no és trobat.
     * Verifica que es llança {@link ExemplarNotFoundException}.
     */
    @Test
    void updateExemplar_shouldThrowException_whenNotFound() {

        when(exemplarRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ExemplarNotFoundException.class, () -> {
            exemplarService.updateExemplar(99L, new Exemplar());
        }, "Exemplar amb ID 99 no trobat.");
    }

    /**
     * Prova l'eliminació reeixida d'un exemplar existent.
     * Verifica que es crida a {@code findById()} i {@code deleteById()} del repositori.
     *
     * @throws ExemplarNotFoundException Si l'exemplar no és trobat.
     */
    @Test
    void deleteExemplar_shouldCallDelete_whenFound() throws ExemplarNotFoundException {

        when(exemplarRepository.findById(1L)).thenReturn(Optional.of(exemplar1));
        doNothing().when(exemplarRepository).deleteById(1L);

        exemplarService.deleteExemplar(1L);

        verify(exemplarRepository, times(1)).findById(1L);
        verify(exemplarRepository, times(1)).deleteById(1L);
    }

    
    //private final List<Exemplar> lliures = Arrays.asList(exemplar1);
    private final List<Exemplar> lliuresByTitol = Arrays.asList(exemplar1);
    //private final List<Exemplar> lliuresByAutor = Arrays.asList(exemplar1);


    /**
     * Prova la lògica de filtratge: si es proporciona un títol, la cerca ha de filtrar només per títol.
     * Verifica que es crida al mètode del repositori específic per títol i s'ignoren altres paràmetres.
     */
    @Test
    void findExemplarsLliuresByTitolOrAutor_shouldFilterByTitol_whenTitolIsPresent() {

        String titol = "A Titol";
        when(exemplarRepository.findExemplarsLliuresByLlibreTitol(titol)).thenReturn(lliuresByTitol);

        List<Exemplar> result = exemplarService.findExemplarsLliuresByTitolOrAutor(titol, "Un Autor");

        assertEquals(lliuresByTitol, result);

        verify(exemplarRepository, times(1)).findExemplarsLliuresByLlibreTitol(titol);
        verify(exemplarRepository, never()).findExemplarsLliuresByAutorNom(anyString());
        verify(exemplarRepository, never()).findExemplarsLliures();
    }

}
