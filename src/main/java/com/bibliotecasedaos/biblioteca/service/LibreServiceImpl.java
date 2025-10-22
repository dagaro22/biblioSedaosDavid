/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.error.LlibreNotFoundException;
import com.bibliotecasedaos.biblioteca.repository.LlibreRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dg
 */
@Service
public class LibreServiceImpl implements LlibreService{
   
    @Autowired
    LlibreRepository llibreRepository;
    
    @Override
    public List<Llibre> findAllLlibres() {
        return llibreRepository.findAllByOrderByTitolAsc(); 
    }


    @Override
    public Llibre saveLlibre(Llibre llibre) {
        return llibreRepository.save(llibre);
    }

    @Override
    public Llibre updateLlibre(Long id, Llibre llibre) throws LlibreNotFoundException {

        Llibre llibreDb = llibreRepository.findById(id)
             .orElseThrow(() -> new LlibreNotFoundException("Llibre amb ID " + id + " no trobat."));
        
        if (Objects.nonNull(llibre.getTitol()) && !llibre.getTitol().isBlank()) {
            llibreDb.setTitol(llibre.getTitol());
        }
        
        return llibreRepository.save(llibreDb);
    }

   
    @Override
    public void deleteLlibre(Long id) throws LlibreNotFoundException {
        llibreRepository.findById(id)
            .orElseThrow(() -> new LlibreNotFoundException("Llibre amb ID " + id + " no trobat."));
        
        llibreRepository.deleteById(id);
    }

    @Override
    public Llibre findLlibreById(Long id) throws LlibreNotFoundException {
        return llibreRepository.findById(id)
            .orElseThrow(() -> new LlibreNotFoundException("Llibre amb ID " + id + " no trobat."));
    }
}
