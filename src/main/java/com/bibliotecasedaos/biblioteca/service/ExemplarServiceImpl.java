/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import com.bibliotecasedaos.biblioteca.entity.Llibre;
import com.bibliotecasedaos.biblioteca.repository.ExemplarRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dg
 */
@Service
public class ExemplarServiceImpl implements ExemplarService{

    @Autowired
    ExemplarRepository exemplarRepository;
    
    @Override
    public List<Exemplar> findAllExemplars() {
        return exemplarRepository.findAll();
    }

    @Override
    public Exemplar saveExemplar(Exemplar exemplar) {
        return exemplarRepository.save(exemplar);
    }

    @Override
    public Exemplar updateExemplar(Long id, Exemplar exemplar) {
        Exemplar exemplarDb = exemplarRepository.findById(id).get();
        if (Objects.nonNull(exemplar.getReservat()) && !"".equalsIgnoreCase(exemplar.getReservat())) {
            exemplarDb.setReservat(exemplar.getReservat());
        }
        
        if (Objects.nonNull(exemplar.getLloc()) && !"".equalsIgnoreCase(exemplar.getLloc())) {
            exemplarDb.setLloc(exemplar.getLloc());
        }
        
        return exemplarRepository.save(exemplarDb);
    }

    @Override
    public void deleteExemplar(Long id) {
        exemplarRepository.deleteById(id);
    }

    @Override
    public Exemplar findExemplarById(Long id) {
        return exemplarRepository.findById(id).get();
    }

    @Override
    public List<Exemplar> findExemplarsLliures() {
        return exemplarRepository.findExemplarsLliures();
    }
    
}
