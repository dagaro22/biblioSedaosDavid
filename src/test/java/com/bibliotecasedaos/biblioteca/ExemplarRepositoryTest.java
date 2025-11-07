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
import jakarta.persistence.EntityManager;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Proves unitaries per a la interfície {@code ExemplarRepository}.
 * 
 * @author David García Rodríguez
 */
@DataJpaTest
@ActiveProfiles("test") 
public class ExemplarRepositoryTest {
    
    @Autowired
    private ExemplarRepository exemplarRepository;

    @Autowired
    private EntityManager entityManager;
    private Autor autor1;
    private Autor autor2;
    private Llibre llibre1;
    private Llibre llibre2;
    private Exemplar exemplarLliure1;
    private Exemplar exemplarReservat;
    private Exemplar exemplarLliure2;

    @BeforeEach
    void setUp() {

        autor1 = new Autor();
        autor1.setNom("George Orwell");
        
        autor2 = new Autor();
        autor2.setNom("Aldous Huxley");

        llibre1 = new Llibre();
        llibre1.setTitol("1984");
        llibre1.setAutor(autor1); 
        llibre1.setEditorial("Secker & Warburg");
        llibre1.setIsbn("9780451524935");
        llibre1.setPagines(328); // Corregido: pagines >= 1
        
        llibre2 = new Llibre();
        llibre2.setTitol("Un món feliç");
        llibre2.setAutor(autor2);
        llibre2.setEditorial("Chatto & Windus");
        llibre2.setIsbn("9780060850524");
        llibre2.setPagines(257); 

        entityManager.persist(autor1);
        entityManager.persist(autor2);
    
        entityManager.persist(llibre1);
        entityManager.persist(llibre2);
            
        exemplarLliure1 = new Exemplar();
        exemplarLliure1.setLlibre(llibre1);
        exemplarLliure1.setLloc("A1-1");
        exemplarLliure1.setReservat("lliure");
        
        exemplarReservat = new Exemplar();
        exemplarReservat.setLlibre(llibre1);
        exemplarReservat.setLloc("A1-2");
        exemplarReservat.setReservat("reservat");
        
        exemplarLliure2 = new Exemplar();
        exemplarLliure2.setLlibre(llibre2);
        exemplarLliure2.setLloc("B2-1");
        exemplarLliure2.setReservat("lliure");
        
        entityManager.persist(exemplarLliure1);
        entityManager.persist(exemplarReservat);
        entityManager.persist(exemplarLliure2);
        
        entityManager.flush(); 
        entityManager.clear();
    }
    
    /**
     * Prova la funcionalitat de recuperar tots els exemplars amb l'estat "lliure".
     * Verifica que es retornen exclusivament els exemplars lliures (2 en aquest cas)
     * i que els exemplars reservats són exclosos.
     */
    @Test
    void findExemplarsLliures_shouldReturnOnlyFreeCopies() {

        List<Exemplar> lliures = exemplarRepository.findExemplarsLliures();

        assertThat(lliures).isNotNull();
        assertThat(lliures).hasSize(2); 
        assertThat(lliures).doesNotContain(exemplarReservat);
        assertThat(lliures.stream().map(Exemplar::getLloc)).containsExactlyInAnyOrder(exemplarLliure1.getLloc(), exemplarLliure2.getLloc());
    }
      
    /**
     * Prova la cerca d'exemplars lliures per títol de llibre, ignorant majúscules/minúscules.
     * Verifica que la cerca amb un títol parcial o amb majúscules/minúscules diferents
     * retorna el resultat correcte.
     */
    @Test
    void findExemplarsLliuresByLlibreTitol_shouldBeCaseInsensitive() {

        List<Exemplar> result = exemplarRepository.findExemplarsLliuresByLlibreTitol("MÓn");

        assertThat(result).hasSize(1);
    }
    
    /**
     * Prova que la cerca per títol només retorna les còpies que estan "lliures".
     * 
     */
    @Test
    void findExemplarsLliuresByLlibreTitol_shouldNotReturnReservedCopies() {

        List<Exemplar> result = exemplarRepository.findExemplarsLliuresByLlibreTitol("1984");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLloc()).isEqualTo(exemplarLliure1.getLloc()); 
    }
    
    /**
     * Prova la cerca d'exemplars lliures per nom d'autor, ignorant majúscules/minúscules.
     * Verifica que la cerca amb nom d'autor en minúscules retorna els exemplars correctes.
     */
    @Test
    void findExemplarsLliuresByAutorNom_shouldReturnCopiesByAuthorIgnoringCase() {

        List<Exemplar> result = exemplarRepository.findExemplarsLliuresByAutorNom("orwell");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLlibre().getTitol()).isEqualTo("1984");
    }

    
    /**
     * Prova que la cerca per nom d'autor només retorna les còpies que estan "lliures".
     * Verifica que l'exemplar reservat del llibre de l'autor donat és exclòs del resultat.
     */
    @Test
    void findExemplarsLliuresByAutorNom_shouldNotReturnReservedCopies() {

        List<Exemplar> result = exemplarRepository.findExemplarsLliuresByAutorNom("George Orwell");

        assertThat(result).hasSize(1);
        assertThat(result.stream().map(Exemplar::getLloc)).contains(exemplarLliure1.getLloc());
        assertThat(result.stream().map(Exemplar::getLloc)).doesNotContain(exemplarReservat.getLloc());
    }
}
