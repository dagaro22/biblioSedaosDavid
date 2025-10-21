/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Llibre;
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
        return llibreRepository.findAll();
    }

    @Override
    public Llibre saveLlibre(Llibre llibre) {
        return llibreRepository.save(llibre);
    }

    @Override
    public Llibre updateLlibre(Long id, Llibre llibre) {
        Llibre llibreDb = llibreRepository.findById(id).get();
        if (Objects.nonNull(llibre.getTitol()) && !"".equalsIgnoreCase(llibre.getTitol())) {
            llibreDb.setTitol(llibre.getTitol());
        }
        
        return llibreRepository.save(llibreDb);
    }

    @Override
    public void deleteLlibre(Long id) {
        llibreRepository.deleteById(id);
    }

    @Override
    public Llibre findLlibreById(Long id) {
        return llibreRepository.findById(id).get();
    }
    
}
