/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.repository.AutorRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Proves unitaries per a la interfície {@link AutorRepository}. 
 * 
 * @author David García Rodríguez
 */
@SpringBootTest
public class AutorRepositoryTest {
    
    @Autowired 
    AutorRepository autorRepository;
    
    /**
     * Prova la funcionalitat de guardar una nova entitat {@link Autor} a la base de dades.
     */
    @Test
    public void saveAutor() {
        Autor autor = new Autor();
        autor.setNom("Maximo Huerta10");
        
        autorRepository.save(autor);
    }
    
    /**
     * Prova la funcionalitat de buscar una entitat {@link Autor} pel seu ID.
     */
    @Test 
    public void findAutorById() {
        Long id = 4L;
        Optional<Autor> autor = autorRepository.findById(id);
        
        System.out.println("Autor: " + autor);       
        
    }
    
    /**
     * Prova la funcionalitat de recuperar tots els autors de la base de dades.
     */
    @Test 
    public void findAllAutors() {
        Long id = 4L;
        List<Autor> autors = autorRepository.findAll();
        Optional<Autor> autor = autorRepository.findById(id);
        
        System.out.println("Autor: " + autors);
        
        
    }
}
