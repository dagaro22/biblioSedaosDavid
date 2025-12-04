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
 * Classe que proporciona la lògica de negoci per a la gestió dels horaris, implementant les operacions de cerca i manteniment de dades.
 * 
 * @author David García Rodríguez
 */
@Service
public class HorariServiceImpl implements HorariService{

    
    @Autowired
    HorariRepository horariRepository;
    
    /**
     * Troba tots els horaris del repositori.
     * @return Una llista de tots els objectes Horari, pot estar buida.
     */
    @Override
    public List<Horari> findAllHoraris() {
        
        return horariRepository.findAll();
        
    }

    /**
     * Guarda un nou horari a la base de dades
     * @param horari L'objecte Horari a desar.
     * @return L'objecte Horari que ha estat desat i persisteix a la base de dades.
     */
    @Override
    public Horari saveHorari(Horari horari) {
        return horariRepository.save(horari);
    }
    
}
