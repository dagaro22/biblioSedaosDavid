/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Grup;
import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.GrupNotFoundException;
import com.bibliotecasedaos.biblioteca.error.HorariReservatException;
import com.bibliotecasedaos.biblioteca.repository.GrupRepository;
import com.bibliotecasedaos.biblioteca.repository.HorariRepository;
import com.bibliotecasedaos.biblioteca.repository.UsuariRepository;
import com.bibliotecasedaos.biblioteca.service.GrupServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Proves unitàries per a la implementació del servei {@link GrupServiceImpl}.
 * 
 * @author David García Rodríguez
 */
@ExtendWith(MockitoExtension.class)
public class GrupServiceImplTest {
    
    @Mock
    private GrupRepository grupRepository;

    @Mock
    private HorariRepository horariRepository;

    @Mock
    private UsuariRepository usuariRepository;

    @InjectMocks
    private GrupServiceImpl grupService;
    
    private Horari horariLliure;
    private Horari horariReservat;
    private Grup grup;
    private Usuari admin;
    private Usuari membre1;
    private Usuari membre2;
    
    @BeforeEach
    void setUp() {

        horariLliure = Horari.builder().id(10L).estat("lliure").build();
        horariReservat = Horari.builder().id(11L).estat("reservat").build();
        admin = Usuari.builder().id(1L).nom("Admin").build();
        membre1 = Usuari.builder().id(2L).nom("Membre 1").build();
        membre2 = Usuari.builder().id(3L).nom("Membre 2").build();
        grup = Grup.builder()
                .id(100L)
                .nom("Grup Prova")
                .horari(horariLliure)
                .administrador(admin)
                .membres(new ArrayList<>(List.of(admin, membre1))) // 2 membres inicials
                .build();
    }
    
    /**
     * Prova salvar un grup.
     * @throws Exception
     */
    @Test
    void testGruardarGrup() throws Exception {

        Grup grupASave = Grup.builder().id(null).nom("Nou Grup").horari(horariLliure).build();
        
        when(horariRepository.findById(10L)).thenReturn(Optional.of(horariLliure));
        when(grupRepository.save(grupASave)).thenReturn(grup);
        when(horariRepository.save(any(Horari.class))).thenReturn(horariReservat);

        Grup grupGuardat = grupService.saveGrup(grupASave);

        assertThat(grupGuardat).isEqualTo(grup);
        assertThat(horariLliure.getEstat()).isEqualTo("reservat");
        
        verify(horariRepository, times(1)).findById(10L);
        verify(horariRepository, times(1)).save(horariLliure);
        verify(grupRepository, times(1)).save(grupASave);
    }
    
    /**
     * Prova salvar un grup amb un horari en estat "reservat".
     * @throws HorariReservatException
     */
    @Test
    void testGuardarGrup_HorariReservatException() {

        Grup grupASave = Grup.builder().horari(horariReservat).build();
        when(horariRepository.findById(11L)).thenReturn(Optional.of(horariReservat));

        assertThrows(HorariReservatException.class, () -> {
            grupService.saveGrup(grupASave);
        });
        
        verify(horariRepository, times(1)).findById(11L);
        verify(horariRepository, never()).save(horariReservat);
        verify(grupRepository, never()).save(any(Grup.class));
    }
    
    /**
     * Prova eliminar un grup, l'horari pasa a estat "lliure"
     * @throws Exception 
     */
    @Test
    void testEliminarGrup() throws Exception {

        Horari horariPerEliminar = Horari.builder().id(12L).estat("reservat").build();
        Grup grupAEliminar = Grup.builder().id(101L).horari(horariPerEliminar).build();
        
        when(grupRepository.findById(101L)).thenReturn(Optional.of(grupAEliminar));

        grupService.deleteGrup(101L);

        assertThat(horariPerEliminar.getEstat()).isEqualTo("lliure");
        
        verify(grupRepository, times(1)).findById(101L);
        verify(horariRepository, times(1)).save(horariPerEliminar);
        verify(grupRepository, times(1)).deleteById(101L);
    }
    
    /**
     * Prova la recuperació de tots els grups.
     */
    @Test
    void testLlistarGrups() {

        List<Grup> grupsLlista = List.of(grup);
        when(grupRepository.findAll()).thenReturn(grupsLlista);

        List<Grup> resultat = grupService.findAllGrups();

        assertThat(resultat).hasSize(1);
        assertThat(resultat.get(0).getNom()).isEqualTo("Grup Prova");
        verify(grupRepository, times(1)).findAll();
    }
    
    /**
     * Prova afegir un membre a un grup.
     * @throws Exception
     */
    @Test
    void testAfegirUsuariGrup() throws Exception {

        Usuari membre3 = Usuari.builder().id(3L).nom("Membre 3 Nou").build();
        
        when(grupRepository.findById(100L)).thenReturn(Optional.of(grup));
        when(usuariRepository.findById(3L)).thenReturn(Optional.of(membre3));

        when(grupRepository.save(grup)).thenReturn(grup);

        Grup grupActualitzat = grupService.afegirUsuariGrup(100L, 3L);

        assertThat(grupActualitzat.getMembres()).hasSize(3);
        assertThat(grupActualitzat.getMembres().stream().anyMatch(u -> u.getId().equals(3L))).isTrue();
        verify(grupRepository, times(1)).save(grup);
    }
    
    /**
     * Prova afegir un membre a un grup que no existeix.
     * @throws GrupNotFoundException
     */
    @Test
    void testAfegirUsuariGrup_GrupNotFoundException() {

        when(grupRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GrupNotFoundException.class, () -> {
            grupService.afegirUsuariGrup(99L, 3L);
        });

        verify(usuariRepository, never()).findById(anyLong());
        verify(grupRepository, never()).save(any(Grup.class));
    }
    
    /**
     * Prova la recuperació de tots els membres d'un grup.
     * @throws Exception
     */
    @Test
    void testTrobarMemebresGrup() throws Exception {

        when(grupRepository.findById(100L)).thenReturn(Optional.of(grup));

        List<Usuari> membres = grupService.trobarMemebresGrup(100L);

        assertThat(membres).hasSize(2);
        assertThat(membres.get(0).getId()).isEqualTo(1L);
        assertThat(membres.get(1).getId()).isEqualTo(2L);
        verify(grupRepository, times(1)).findById(100L);
    }
    
    /**
     * Prova l'eliminació d'un grup existent per ID.
     * @throws Exception.
     */
    @Test
    void testEliminarUsuariDeGrup() throws Exception {

        when(grupRepository.findById(100L)).thenReturn(Optional.of(grup));

        grupService.eliminarUsuariDeGrup(100L, 2L);

        assertThat(grup.getMembres()).hasSize(1);
        assertThat(grup.getMembres().get(0).getId()).isEqualTo(1L);
        verify(grupRepository, times(1)).findById(100L);
        verify(grupRepository, times(1)).save(grup);
    }
    
}
