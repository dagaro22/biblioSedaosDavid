/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.repository.HorariRepository;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Proves unitaries per a la interfície {@link HorariRepository}.
 * 
 * @author David García Rodríguez
 */
@DataJpaTest
public class HorariRepositoryTest {
    
    @Autowired
    private HorariRepository horariRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private Horari nouHorari(String sala, String dia, String hora, String estat) {
        Horari horari = new Horari();
        horari.setSala(sala);
        horari.setDia(dia);
        horari.setHora(hora);
        horari.setEstat(estat);
        
        return entityManager.persistAndFlush(horari);       
    }
    
    /**
     * Comprova que el mètode {@code findAll} retorna tots els horaris.
     */
    @Test
    void testListarTotsElsHoraris() {
        nouHorari("A", "dilluns", "16h","lliure");
        nouHorari("B", "dimarts", "18h","lliure");

        List<Horari> horaris = horariRepository.findAll();

        assertThat(horaris).isNotNull();
        assertThat(horaris).hasSize(2);
        assertThat(horaris)
            .extracting(Horari::getDia)
            .containsExactlyInAnyOrder("dilluns", "dimarts");
    }
    
    /**
     * Prova guardar un horari al repositori.
     */
    @Test
    void testGuardarHoari() {
        Horari horari = Horari.builder()
                .sala("A")
                .dia("dijous")
                .hora("18h")
                .estat("lliure")
                .build();
        
        Horari horariDb = horariRepository.save(horari);
        
        assertThat(horariDb).isNotNull();
        assertThat(horariDb.getId()).isNotNull();
        assertThat(horariDb.getDia()).isEqualTo("dijous");
    }
}
