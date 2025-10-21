/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.entity.Prestec;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.repository.ExemplarRepository;
import com.bibliotecasedaos.biblioteca.repository.PrestecRepository;
import com.bibliotecasedaos.biblioteca.repository.UsuariRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author dg
 */
@SpringBootTest
public class PrestecRepositoryTest {
    
    @Autowired
    PrestecRepository prestecRepository;
    
    @Autowired 
    ExemplarRepository exemplarRepository;
    
    @Autowired
    UsuariRepository usuariRepository;
    
    @Test
    public void SavePrestec() {
        Exemplar exemplar = exemplarRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Exemplar amb ID " + 3L + " no trobat."));
        
        Usuari usuari = usuariRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Usuari amb ID " + 1L + " no trobat."));
        
        Prestec prestec = Prestec.builder()
                .dataPrestec(LocalDate.now())
                .dataDevolucio(null)
                .exemplar(exemplar)
                .usuari(usuari)
                .build();
        
        Prestec prestecGuardat = prestecRepository.save(prestec);
        
        assertThat(prestecGuardat).isNotNull();
        assertThat(prestecGuardat.getId()).isNotNull();
        assertThat(prestecGuardat.getExemplar().getId()).isNotNull();
        assertThat(prestecGuardat.getUsuari().getId()).isNotNull();
        
    }
}
