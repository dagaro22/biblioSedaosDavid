/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.repository.AutorRepository;
import com.bibliotecasedaos.biblioteca.repository.ExemplarRepository;
import com.bibliotecasedaos.biblioteca.repository.LlibreRepository;
import com.bibliotecasedaos.biblioteca.repository.PrestecRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author dg
 */

@SpringBootTest
@Transactional
@ActiveProfiles("test") 
public class LlibreRepositoryTest {
    
    
    @Autowired
    private LlibreRepository llibreRepository;
    
    @Autowired 
    private AutorRepository autorRepository;
    
    @Autowired 
    private ExemplarRepository exemplarRepository;
    
    @Autowired 
    private PrestecRepository prestecRepository;

    @Autowired
    private EntityManager entityManager;

    private Autor autor1;
    private Llibre llibreA;
    private Llibre llibreB;
    private Llibre llibreC;

    @BeforeEach
    void setUp() {

        prestecRepository.deleteAllInBatch();
        exemplarRepository.deleteAllInBatch();
        llibreRepository.deleteAllInBatch();
        entityManager.flush();
  
        autor1 = new Autor();
        autor1.setNom("Autor Test");
        entityManager.persist(autor1);

        llibreA = new Llibre();
        llibreA.setTitol("Zarramundi"); 
        llibreA.setIsbn("1111");
        llibreA.setAutor(autor1);
        llibreA.setEditorial("Minotauro"); 
        llibreA.setPagines(150);
        entityManager.persist(llibreA);


        llibreB = new Llibre();
        llibreB.setTitol("Barcelona"); 
        llibreB.setIsbn("2222");
        llibreB.setAutor(autor1);
        llibreB.setEditorial("Minotauro"); 
        llibreB.setPagines(200); // Mínim 1
        entityManager.persist(llibreB);
        
        llibreC = new Llibre();
        llibreC.setTitol("Casa encantada"); 
        llibreC.setIsbn("3333");
        llibreC.setAutor(autor1);
        llibreC.setEditorial("Minotauro"); 
        llibreC.setPagines(50); // Mínim 1
        entityManager.persist(llibreC);

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * Comprova que el mètode {@code findAllByOrderByTitolAsc} retorna tots els llibres
     * i els ordena correctament pel camp 'titol' en ordre ascendent.
     */
    @Test
    void findAllByOrderByTitolAsc_shouldReturnLlistatOrdenatPerTitol() {
 
        List<Llibre> result = llibreRepository.findAllByOrderByTitolAsc();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        assertThat(result.get(0).getTitol()).isEqualTo("Barcelona"); 
        assertThat(result.get(1).getTitol()).isEqualTo("Casa encantada"); 
        assertThat(result.get(2).getTitol()).isEqualTo("Zarramundi"); 
    }
    
    /**
     * Comprova que el mètode retorna una llista buida si no hi ha llibres al repositori.
     */
     @Test
     void findAllByOrderByTitolAsc_shouldReturnEmptyLlistat_whenNoLlibresExist() {

         llibreRepository.deleteAll(); 
         entityManager.flush();

         List<Llibre> result = llibreRepository.findAllByOrderByTitolAsc();

         assertThat(result).isNotNull();
         assertThat(result).isEmpty();
     }
}
