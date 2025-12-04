/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Grup;
import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.repository.GrupRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Proves unitaries per a la interfície {@link GrupRepository}. 
 * 
 * @author David García Rodríguez
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY) 
public class GrupRepositoryTest {
    @Autowired
    private GrupRepository grupRepository;

    @Autowired
    private EntityManager entityManager;
    
    private Usuari usuariAdmin;
    private Horari horariGrup;
    private Horari horariGrupB;
    private Usuari usuariMembre;
    
    @BeforeEach
    void setUp() {

        usuariAdmin = Usuari.builder()
                .nom("Admin Test")
                .cognom1("cognom1")
                .nick("aaaa")
                .nif("11111111A")
                .carrer("ssss")
                .cp("08666")
                .localitat("asdssd")
                .provincia("aaaaa")
                .email("admingrup@gmail.com")
                .tlf("633333323")
                .rol(1)
                .password("sdssdsddsdds")
                .build();
        entityManager.persist(usuariAdmin);
        
        horariGrup = new Horari();
        horariGrup.setSala("A");
        horariGrup.setDia("dilluns");
        horariGrup.setHora("12h");
        horariGrup.setEstat("lliure");       
        entityManager.persist(horariGrup);
        
        horariGrupB = new Horari();
        horariGrupB.setSala("A");
        horariGrupB.setDia("dilluns");
        horariGrupB.setHora("14h");
        horariGrupB.setEstat("lliure");       
        entityManager.persist(horariGrupB);

        usuariMembre = Usuari.builder()
                .nom("Membre Test")
                .cognom1("cognom1")
                .nick("bbbb")
                .nif("11111111B")
                .carrer("ssss")
                .cp("08666")
                .localitat("asdssd")
                .provincia("aaaaa")
                .email("membre@gmail.com")
                .tlf("633333333")
                .rol(1)
                .password("sdssdsddsdds")
                .build();
        entityManager.persist(usuariMembre);
        
        entityManager.flush();
        entityManager.clear();
    }
    
    private Grup crearGrupValid(String nom, String tematica, Horari horari) {
        return Grup.builder()
                .nom(nom)
                .tematica(tematica)
                .administrador(usuariAdmin) 
                .horari(horari)
                .membres(new ArrayList<>(List.of(usuariMembre)))
                .build();
    }
    
    /**
     * Prova la funcionalitat de creació i persistència d'una nova entitat {@code Grup}.
     */
    @Test
    void testCrearGrup() {

        Grup grup = crearGrupValid("Grup Novel·les Negres", "Misteri", horariGrup); 
        Grup grupDb = grupRepository.save(grup);
        
        entityManager.flush(); 
        entityManager.clear(); 

        Grup grupVerificat = entityManager.find(Grup.class, grupDb.getId());

        assertThat(grupDb).isNotNull();
        assertThat(grupVerificat.getNom()).isEqualTo("Grup Novel·les Negres");       
        assertThat(grupVerificat.getAdministrador().getId()).isEqualTo(usuariAdmin.getId());
        assertThat(grupVerificat.getMembres()).hasSize(1);
        assertThat(grupVerificat.getMembres().get(0).getId()).isEqualTo(usuariMembre.getId());
    }
    
    /**
     * Prova la funcionalitat d'eliminació d'una entitat {@code Grup} pel seu ID.
     */
    @Test
    void testEliminarGrup() {

        Grup grup = crearGrupValid("Grup a Esborrar", "Per eliminar", horariGrup);
        entityManager.persist(grup);
        entityManager.flush();
        Long id = grup.getId();

        grupRepository.deleteById(id);

        Grup resultat = entityManager.find(Grup.class, id);
        assertThat(resultat).isNull();
    }
    
    /**
     * Prova la recuperació de tots els grups existents a la base de dades.
     */
    @Test
    void testLlistarTotsElsGrups() {

        Grup g1 = crearGrupValid("Grup cuina", "Cuina", horariGrup);
        Grup g2 = crearGrupValid("Grup Clàssics", "Literatura", horariGrupB);
        
        entityManager.persist(g1);
        entityManager.persist(g2);
        
        entityManager.flush();
        entityManager.clear();

        List<Grup> llistaGrups = grupRepository.findAll();

        assertThat(llistaGrups).isNotEmpty();
        assertThat(llistaGrups).hasSize(2); 
        
        assertThat(llistaGrups)
            .extracting(Grup::getNom)
            .containsExactlyInAnyOrder("Grup cuina", "Grup Clàssics");
    }
    
    /**
     * Prova la recuperació de tots els membres d'un grup existents a la base de dades.
     */
    @Test
    void testLlistarMembresGrup() {

        Grup grup = crearGrupValid("Grup amb Membres", "Lectura", horariGrup);
        Grup grupGuardat = grupRepository.save(grup);
        entityManager.flush();
        entityManager.clear();

        Grup grupDb = grupRepository.findById(grupGuardat.getId()).orElseThrow();
        List<Usuari> membres = grupDb.getMembres();
        
        assertThat(membres).isNotNull();
        assertThat(membres).hasSize(1);
        assertThat(membres.get(0).getNom()).isEqualTo(usuariMembre.getNom());
        assertThat(membres.get(0).getId()).isEqualTo(usuariMembre.getId());
    }
    
    /**
     * Prova l'actualització d'un grup per afegir un nou usuari a la seva llista de membres.
     */
    @Test
    void testAfegirMembreAGrupExistents() {

        Usuari usuariNouMembre = Usuari.builder()
                .nom("Nou Membre")
                .cognom1("cognomNou")
                .nick("noumem")
                .nif("33333333C")
                .localitat("asdsdsd")
                .provincia("asdasdsad")
                .carrer("sdasdsasddds")
                .password("ssdsdawd")
                .email("noumembre@gmail.com")
                .tlf("688888888")
                .cp("08650")
                .rol(1)
                .build();
        entityManager.persist(usuariNouMembre);
        
        Grup grupInicial = crearGrupValid("Grup Ficció", "Ciència Ficció", horariGrup);
        Grup grupGuardat = grupRepository.save(grupInicial);
        entityManager.flush();
        entityManager.clear(); 
        
        Long grupId = grupGuardat.getId();
        Grup grupAActualitzar = grupRepository.findById(grupId).orElseThrow();
        grupAActualitzar.getMembres().add(usuariNouMembre);
        
        grupRepository.save(grupAActualitzar);
        entityManager.flush();
        entityManager.clear(); 

        Grup grupDb = grupRepository.findById(grupId).orElseThrow();

        assertThat(grupDb.getMembres()).hasSize(2); 

        assertThat(grupDb.getMembres())
            .extracting(Usuari::getNom)
            .contains("Membre Test", "Nou Membre");          
        assertThat(grupDb.getMembres())
            .extracting(Usuari::getId)
            .contains(usuariNouMembre.getId());
    }
}
