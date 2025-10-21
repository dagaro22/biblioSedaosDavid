/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.repository.AutorRepository;
import com.bibliotecasedaos.biblioteca.repository.ExemplarRepository;
import com.bibliotecasedaos.biblioteca.repository.LlibreRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author dg
 */
@SpringBootTest
public class ExemplarRepositoryTest {
    
    @Autowired
    ExemplarRepository exemplarRepository;
    
    @Autowired
    LlibreRepository llibreRepository;
    
    
    
    @Test
    public void SaveExemplar() {
        
        final long ID_LLIBRE = 1L;
        Llibre llibreReservat = llibreRepository.findById(ID_LLIBRE)
            .orElseThrow(() -> new RuntimeException("Llibre amb ID " + ID_LLIBRE + " no trobat."));
        
        Exemplar exemplar = Exemplar.builder()
                .lloc("e3-b4")
                .reservat("reservat")
                .llibre(llibreReservat)
                .build();
        
        Exemplar exemplarGuardat = exemplarRepository.save(exemplar);
        
        assertThat(exemplarGuardat).isNotNull();
        assertThat(exemplarGuardat.getId()).isNotNull();
        assertThat(exemplarGuardat.getLlibre().getId()).isEqualTo(ID_LLIBRE);
    }
    
}
