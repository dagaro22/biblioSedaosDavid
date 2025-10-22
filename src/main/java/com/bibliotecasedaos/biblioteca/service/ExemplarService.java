/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.error.ExemplarNotFoundException;
import java.util.List;

/**
 *
 * @author dg
 */
public interface ExemplarService {
    
    List<Exemplar> findAllExemplars();
    Exemplar saveExemplar(Exemplar exemplar);
    Exemplar updateExemplar(Long id, Exemplar exemplar) throws ExemplarNotFoundException;
    void deleteExemplar(Long id) throws ExemplarNotFoundException;   
    Exemplar findExemplarById(Long id) throws ExemplarNotFoundException;
    List<Exemplar> findExemplarsLliuresByTitolOrAutor(String titol, String autorNom);
}
