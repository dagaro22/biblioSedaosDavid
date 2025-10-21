/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Llibre;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author dg
 */
public interface LlibreService {
    
    List<Llibre> findAllLlibres();
    
    Llibre saveLlibre(Llibre llibre);
    Llibre updateLlibre(Long id, Llibre llibre);
    void deleteLlibre(Long id);   
    Llibre findLlibreById(Long id);
}
