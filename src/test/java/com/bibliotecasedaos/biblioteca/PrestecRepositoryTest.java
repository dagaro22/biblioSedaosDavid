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
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * Proves unitaries per a la interfície {@link PrestecRepository}
 * @author David García Rodríguez
 */
@DataJpaTest
@ActiveProfiles("test")
public class PrestecRepositoryTest {

    @Autowired
    private PrestecRepository prestecRepository;
    
    @Autowired
    private UsuariRepository usuariRepository;
    
    @Autowired
    private ExemplarRepository exemplarRepository;

    @Autowired
    private EntityManager entityManager; 

    private Usuari usuariActiu;
    private Usuari usuariInactiu;
    private Exemplar exemplar1;
    private Exemplar exemplar2;
    private Prestec prestecActiu1;
    private Prestec prestecActiu2;
    private Prestec prestecFinalitzat;

    @BeforeEach
    void setUp() {

        usuariActiu = new Usuari();
        usuariActiu.setNom("Laura");
        usuariActiu.setCognom1("García");
        usuariActiu.setNif("12345678A");
        usuariActiu.setEmail("laura@test.com");
        usuariActiu.setNick("laurag");
        usuariActiu.setPassword("pass123");
        usuariActiu.setTlf("600111222");
        usuariActiu.setCarrer("Carrer Fals 123");
        usuariActiu.setCp("08001");
        usuariActiu.setLocalitat("Barcelona");
        usuariActiu.setProvincia("Barcelona");

        usuariInactiu = new Usuari();
        usuariInactiu.setNom("Pere");
        usuariInactiu.setCognom1("Martinez");
        usuariInactiu.setNif("87654321B");
        usuariInactiu.setEmail("pere@test.com");
        usuariInactiu.setNick("perem");
        usuariInactiu.setPassword("pass456");
        usuariInactiu.setTlf("600333444");
        usuariInactiu.setCarrer("Avinguda Test 45");
        usuariInactiu.setCp("25002");
        usuariInactiu.setLocalitat("Lleida");
        usuariInactiu.setProvincia("Lleida");
        
        exemplar1 = new Exemplar();
        exemplar1.setLloc("E1");
        
        exemplar2 = new Exemplar();
        exemplar2.setLloc("E2");

        usuariActiu = usuariRepository.save(usuariActiu); 
        usuariInactiu = usuariRepository.save(usuariInactiu);
        exemplar1 = exemplarRepository.save(exemplar1);
        exemplar2 = exemplarRepository.save(exemplar2);

        prestecActiu1 = new Prestec();
        prestecActiu1.setUsuari(usuariActiu);
        prestecActiu1.setExemplar(exemplar1);
        prestecActiu1.setDataPrestec(LocalDate.now().minusDays(10));
        prestecActiu1.setDataDevolucio(null);
        
        prestecActiu2 = new Prestec();
        prestecActiu2.setUsuari(usuariActiu);
        prestecActiu2.setExemplar(exemplar2);
        prestecActiu2.setDataPrestec(LocalDate.now().minusDays(5));
        prestecActiu2.setDataDevolucio(null);

        prestecFinalitzat = new Prestec();
        prestecFinalitzat.setUsuari(usuariInactiu);
        prestecFinalitzat.setExemplar(exemplar1);
        prestecFinalitzat.setDataPrestec(LocalDate.now().minusMonths(1));
        prestecFinalitzat.setDataDevolucio(LocalDate.now().minusWeeks(1));

        prestecRepository.saveAll(List.of(prestecActiu1, prestecActiu2, prestecFinalitzat));
        
    }

    @AfterEach
    @Transactional
    void tearDown() {
        prestecRepository.deleteAll();
        usuariRepository.deleteAll();
        exemplarRepository.deleteAll();
    }

    /**
     * Prova la funcionalitat de trobar només els préstecs actius per a un ID d'usuari concret.
     */
    @Test
    void findPrestecsActiusByUsuariId_shouldReturnOnlyActiveLoansForUser() {

        List<Prestec> actius = prestecRepository.findPrestecsActiusByUsuariId(usuariActiu.getId());

        assertThat(actius).isNotNull();
        assertThat(actius).hasSize(2); 
    }
    
    /**
     * Prova l'actualització de la data de devolució d'un préstec actiu mitjançant una consulta DML.
     */
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void updateDataDevolucioToCurrentDate_shouldUpdateLoanDate() {

        Long targetId = prestecActiu1.getId();
        LocalDate returnDate = LocalDate.of(2025, 11, 3); 
  
        int filesModificades = prestecRepository.updateDataDevolucioToCurrentDate(targetId, returnDate);

        assertThat(filesModificades).isEqualTo(1);

        Prestec updatedPrestec = prestecRepository.findById(targetId).orElse(null);
    
        assertThat(updatedPrestec).isNotNull();
        assertThat(updatedPrestec.getDataDevolucio()).isEqualTo(returnDate);

        List<Prestec> actiusDespres = prestecRepository.findAllPrestecsActius();
        assertThat(actiusDespres).doesNotContain(updatedPrestec);
}
    /**
     * Prova l'actualització de la data de devolució per a un préstec inexistent.
     */
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void updateDataDevolucioToCurrentDate_shouldReturnZero_whenLoanNotFound() {

        Long inexistentId = 999L;
        LocalDate returnDate = LocalDate.now();

        int filesModificades = prestecRepository.updateDataDevolucioToCurrentDate(inexistentId, returnDate);

        assertThat(filesModificades).isEqualTo(0);
    }
}
