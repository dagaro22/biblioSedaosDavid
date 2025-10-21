/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.repository.AutorRepository;
import com.bibliotecasedaos.biblioteca.repository.LlibreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author dg
 */

@SpringBootTest
public class LlibreRepositoryTest {
    
    @Autowired
    LlibreRepository llibreRepository;
    
    @Autowired
    AutorRepository autorRepository;
    
    @Test
    public void SaveLlibre() {
        
        final long ID_AUTOR = 4L;
        Autor autor = autorRepository.findById(ID_AUTOR)
            .orElseThrow(() -> new RuntimeException("Autor amb ID " + ID_AUTOR + " no trobat."));
        
        Llibre llibre = Llibre.builder()
                .isbn("11111111")
                .titol("El dldldld")
                .pagines(300)
                .editorial("Minotauro")
                .autor(autor)
                .build();
        
        llibreRepository.save(llibre);
    }
}
