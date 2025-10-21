/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.entity.Llibre;
import java.util.List;

/**
 *
 * @author dg
 */
public interface ExemplarService {
    
    List<Exemplar> findAllExemplars();
    List<Exemplar> findExemplarsLliures();
    Exemplar saveExemplar(Exemplar exemplar);
    Exemplar updateExemplar(Long id, Exemplar exemplar);
    void deleteExemplar(Long id);   
    Exemplar findExemplarById(Long id);
}
