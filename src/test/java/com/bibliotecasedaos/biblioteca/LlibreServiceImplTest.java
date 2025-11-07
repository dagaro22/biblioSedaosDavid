/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.error.LlibreNotFoundException;
import com.bibliotecasedaos.biblioteca.repository.AutorRepository;
import com.bibliotecasedaos.biblioteca.repository.LlibreRepository;
import com.bibliotecasedaos.biblioteca.service.LibreServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Proves unitàries per a la implementació del servei {@link LibreServiceImpl}.
 *
 * @author David García Rodríguez
 */
@ExtendWith(MockitoExtension.class)
public class LlibreServiceImplTest {

    @InjectMocks
    private LibreServiceImpl llibreService;

    @Mock
    private LlibreRepository llibreRepository;

    @Mock
    private AutorRepository autorRepository;

    private Llibre llibre;
    private Autor autor;

    @BeforeEach
    void setUp() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNom("George Orwell");

        llibre = new Llibre();
        llibre.setId(10L);
        llibre.setTitol("1984");
        llibre.setIsbn("978-3-16-148410-0");
        llibre.setPagines(328);
        llibre.setEditorial("Secker & Warburg");
        llibre.setAutor(autor);
    }

    /**
     * Prova la recuperació de tots els llibres.
     */
    @Test
    void findAllLlibres_shouldReturnAllLlibres() {

        Llibre llibre2 = new Llibre();
        llibre2.setTitol("Animal Farm");
        List<Llibre> expectedLlibres = Arrays.asList(llibre, llibre2);

        when(llibreRepository.findAllByOrderByTitolAsc()).thenReturn(expectedLlibres);

        List<Llibre> actualLlibres = llibreService.findAllLlibres();

        assertNotNull(actualLlibres);
        assertEquals(2, actualLlibres.size());
        assertEquals("1984", actualLlibres.get(0).getTitol());

        verify(llibreRepository, times(1)).findAllByOrderByTitolAsc();
    }


    /**
     * Prova la cerca d'un llibre per ID quan és trobat.
     * @throws LlibreNotFoundException Si el llibre no és trobat (no hauria d'ocórrer).
     */
    @Test
    void findLlibreById_shouldReturnLlibre_whenFound() throws LlibreNotFoundException {

        when(llibreRepository.findById(10L)).thenReturn(Optional.of(llibre));

        Llibre actualLlibre = llibreService.findLlibreById(10L);

        assertEquals(llibre.getTitol(), actualLlibre.getTitol());
        verify(llibreRepository, times(1)).findById(10L);
    }


    /**
     * Prova el guardat d'un nou llibre.
     */
    @Test
    void saveLlibre_shouldReturnSavedLlibre() {

        when(llibreRepository.save(any(Llibre.class))).thenReturn(llibre);

        Llibre savedLlibre = llibreService.saveLlibre(llibre);

        assertNotNull(savedLlibre);
        assertEquals("1984", savedLlibre.getTitol());
        verify(llibreRepository, times(1)).save(llibre);
    }


    /**
     * Prova l'actualització d'un llibre existent, incloent-hi la modificació de l'autor.
     * @throws LlibreNotFoundException Si el llibre no és trobat.
     */
    @Test
    void updateLlibre_shouldUpdateAllFields_whenLlibreAndAutorExist() throws LlibreNotFoundException {
        // Arrange
        Llibre updateData = new Llibre();
        updateData.setTitol("Updated Title");
        updateData.setIsbn("999-9-99-999999-9");
        updateData.setPagines(500);
        updateData.setEditorial("New Publisher");
        
        Autor newAutor = new Autor();
        newAutor.setId(2L);
        
        updateData.setAutor(newAutor);

        when(llibreRepository.findById(10L)).thenReturn(Optional.of(llibre));
        when(autorRepository.findById(2L)).thenReturn(Optional.of(newAutor));
        when(llibreRepository.save(any(Llibre.class))).thenReturn(llibre);

        Llibre updatedLlibre = llibreService.updateLlibre(10L, updateData);

        assertEquals("Updated Title", updatedLlibre.getTitol());
        assertEquals("999-9-99-999999-9", updatedLlibre.getIsbn());
        assertEquals(500, updatedLlibre.getPagines());
        assertEquals("New Publisher", updatedLlibre.getEditorial());
        assertEquals(2L, updatedLlibre.getAutor().getId());
        
        verify(llibreRepository, times(1)).findById(10L);
        verify(autorRepository, times(1)).findById(2L);
        verify(llibreRepository, times(1)).save(any(Llibre.class));
    }
    

    /**
     * Prova l'eliminació d'un llibre existent per ID.
     * @throws LlibreNotFoundException Si el llibre no és trobat.
     */
    @Test
    void deleteLlibre_shouldDeleteLlibre_whenFound() throws LlibreNotFoundException {

        when(llibreRepository.findById(10L)).thenReturn(Optional.of(llibre));
        doNothing().when(llibreRepository).deleteById(10L);

        llibreService.deleteLlibre(10L);

        verify(llibreRepository, times(1)).findById(10L);
        verify(llibreRepository, times(1)).deleteById(10L);
    }

}
