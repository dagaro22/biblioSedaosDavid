/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.repository.HorariRepository;
import com.bibliotecasedaos.biblioteca.service.HorariServiceImpl;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Proves unitàries per a la implementació del servei {@link HorariServiceImpl}.
 * 
 * @author David García Rodríguez
 */
@ExtendWith(MockitoExtension.class)
public class HorariServiceImplTest {
    
    @Mock
    private HorariRepository horariRepository;
    
    @InjectMocks
    private HorariServiceImpl horariService;
    
    private Horari horariA;
    private Horari horariB;
    
    @BeforeEach
    void setUp() {
        
        horariA = new Horari();
        horariA.setId(1L);
        horariA.setSala("A");
        horariA.setDia("dilluns");
        horariA.setHora("12h");
        horariA.setEstat("lliure");
        
        horariB = new Horari();
        horariB.setId(2L);
        horariB.setSala("B");
        horariB.setDia("dimarts");
        horariB.setHora("14h");
        horariB.setEstat("lliure");
    }
    
    /**
     * Prova la recuperació de TOTS els horaris (reservats i lliures).
     */
    @Test
    void testListarTotsElsHoraris() {
        
        List<Horari> horaris = Arrays.asList(horariA, horariB);
        when(horariRepository.findAll()).thenReturn(horaris);
        
        List<Horari> horarisDb = horariService.findAllHoraris();
        
        assertNotNull(horarisDb);
        assertEquals(2, horarisDb.size());
        verify(horariRepository, times(1)).findAll();
               
    }
    
    /**
     * Prova la creació d'un nou horari.
     */
    @Test 
    void testGuardarHoari() {
        //when(horariRepository.save(horariA)).thenReturn(horariA);
        when(horariRepository.save(any(Horari.class))).thenReturn(horariA);

        Horari horariDb = horariService.saveHorari(horariA);

        assertNotNull(horariDb);
        assertEquals("dilluns", horariDb.getDia());
        verify(horariRepository, times(1)).save(horariA);
    }
}
