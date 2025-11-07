/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.entity.Prestec;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.ExemplarNotFoundException;
import com.bibliotecasedaos.biblioteca.error.ExemplarReservatException;
import com.bibliotecasedaos.biblioteca.error.PrestecNotFoundException;
import com.bibliotecasedaos.biblioteca.repository.ExemplarRepository;
import com.bibliotecasedaos.biblioteca.repository.PrestecRepository;
import com.bibliotecasedaos.biblioteca.service.PrestecServiceImpl;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Proves unitàries per a la implementació del servei {@link PrestecServiceImpl}.
 * 
 * @author David García Rodríguez
 */
@ExtendWith(MockitoExtension.class)
public class PrestecServiceImplTest {

    @Mock
    private PrestecRepository prestecRepository;

    @Mock
    private ExemplarRepository exemplarRepository;

    @InjectMocks
    private PrestecServiceImpl prestecService;

    private final Long USER_ID = 1L;
    private final Long LOAN_ID = 10L;
    private final Long EXEMPLARY_ID = 100L;
    private final Long INVALID_ID = 999L;

    private Usuari usuari;
    private Exemplar exemplar;
    private Prestec prestecActiu;

    @BeforeEach
    void setUp() {

        usuari = new Usuari();
        usuari.setId(USER_ID);

        exemplar = new Exemplar();
        exemplar.setId(EXEMPLARY_ID);
        exemplar.setReservat("prestat"); 

        prestecActiu = new Prestec();
        prestecActiu.setId(LOAN_ID);
        prestecActiu.setUsuari(usuari);
        prestecActiu.setExemplar(exemplar);
        prestecActiu.setDataPrestec(LocalDate.now().minusDays(5));
        prestecActiu.setDataDevolucio(null);
    }


    /**
     * Prova la recuperació de préstecs actius quan es proporciona un ID d'usuari.
     */
    @Test
    void findPrestecsActius_shouldReturnActiveLoans_whenUserIdIsProvided() {

        List<Prestec> expected = Collections.singletonList(prestecActiu);
        when(prestecRepository.findPrestecsActiusByUsuariId(USER_ID)).thenReturn(expected);

        List<Prestec> result = prestecService.findPrestecsActius(USER_ID);

        assertEquals(1, result.size());
        verify(prestecRepository, times(1)).findPrestecsActiusByUsuariId(USER_ID);
        verify(prestecRepository, never()).findAllPrestecsActius();
    }

    /**
     * Prova la recuperació de TOTS els préstecs actius quan no es proporciona un ID d'usuari (assumint rol Administrador).
     */
    @Test
    void findPrestecsActius_shouldReturnAllActiveLoans_whenUserIdIsNull() {

        List<Prestec> expected = Collections.singletonList(prestecActiu);
        when(prestecRepository.findAllPrestecsActius()).thenReturn(expected);

        List<Prestec> result = prestecService.findPrestecsActius(null);

        assertEquals(1, result.size());
        verify(prestecRepository, times(1)).findAllPrestecsActius();
        verify(prestecRepository, never()).findPrestecsActiusByUsuariId(anyLong());
    }

    /**
     * Prova la recuperació de TOTS els préstecs (actius i finalitzats) per a un ID d'usuari concret.
     */
    @Test
    void findAllPrestecs_shouldReturnAllLoansForUser_whenUserIdIsProvided() {

        List<Prestec> expected = Arrays.asList(prestecActiu, new Prestec());
        when(prestecRepository.findAllPrestecsByUsuariId(USER_ID)).thenReturn(expected);

        List<Prestec> result = prestecService.findAllPrestecs(USER_ID);

        assertEquals(2, result.size());
        verify(prestecRepository, times(1)).findAllPrestecsByUsuariId(USER_ID);
        verify(prestecRepository, never()).findAll();
    }

    /**
     * Prova la devolució reeixida d'un préstec.
     * @throws PrestecNotFoundException Si el préstec no existeix o falla l'actualització.
     */
    @Test
    void retornarPrestec_shouldSucceedAndUpdateExemplarState() throws PrestecNotFoundException {

        when(prestecRepository.findById(LOAN_ID)).thenReturn(Optional.of(prestecActiu));
        when(prestecRepository.updateDataDevolucioToCurrentDate(eq(LOAN_ID), any(LocalDate.class))).thenReturn(1);
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplar);

        prestecService.retornarPrestec(LOAN_ID);

        assertEquals("lliure", prestecActiu.getExemplar().getReservat()); 
        verify(prestecRepository, times(1)).findById(LOAN_ID);
        verify(prestecRepository, times(1)).updateDataDevolucioToCurrentDate(eq(LOAN_ID), any(LocalDate.class));
        verify(exemplarRepository, times(1)).save(prestecActiu.getExemplar());
    }


    /**
     * Prova la creació d'un nou préstec quan l'exemplar està disponible ("lliure").
     * @throws Exception Si es produeix alguna excepció no esperada.
     */
    @Test
    void savePrestec_shouldRegisterNewLoan_andUpdateExemplar() throws Exception {

        Exemplar lliureExemplar = new Exemplar();
        lliureExemplar.setId(EXEMPLARY_ID);
        lliureExemplar.setReservat("lliure");
        
        Prestec nouPrestec = new Prestec();
        nouPrestec.setExemplar(lliureExemplar);
        nouPrestec.setUsuari(usuari);

        when(exemplarRepository.findById(EXEMPLARY_ID)).thenReturn(Optional.of(lliureExemplar));
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(lliureExemplar);
        when(prestecRepository.save(nouPrestec)).thenReturn(nouPrestec);

        Prestec savedPrestec = prestecService.savePrestec(nouPrestec);

        assertEquals("prestat", lliureExemplar.getReservat());
        assertNotNull(savedPrestec.getDataPrestec());
        verify(exemplarRepository, times(1)).findById(EXEMPLARY_ID);
        verify(exemplarRepository, times(1)).save(lliureExemplar);
        verify(prestecRepository, times(1)).save(nouPrestec);
    }
    
}
