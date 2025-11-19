/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.repository.HorariRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David García Rodríguez
 */
@Service
public class HorariServiceImpl implements HorariService{

    
    @Autowired
    HorariRepository horariRepository;
    
    @Override
    public List<Horari> findAllHoraris() {
        
        return horariRepository.findAll();
        
    }

    @Override
    public Horari saveHorari(Horari horari) {
        return horariRepository.save(horari);
    }
    
}
