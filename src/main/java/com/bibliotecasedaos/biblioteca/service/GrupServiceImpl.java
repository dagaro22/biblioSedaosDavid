/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Grup;
import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.error.GrupNotFoundException;
import com.bibliotecasedaos.biblioteca.error.HorariNotFoundException;
import com.bibliotecasedaos.biblioteca.error.HorariReservatException;
import com.bibliotecasedaos.biblioteca.repository.GrupRepository;
import com.bibliotecasedaos.biblioteca.repository.HorariRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David García Rodríguez
 */
@Service
public class GrupServiceImpl implements GrupService {

    @Autowired
    GrupRepository grupRepository;
    
    @Autowired 
    HorariRepository horariRepository;
    
    @Override
    @Transactional
    public Grup saveGrup(Grup grup) throws HorariNotFoundException, HorariReservatException {
        
        Long horariId = grup.getHorari().getId();
        Horari horariDb = horariRepository.findById(horariId)
                .orElseThrow(() -> new HorariNotFoundException("L'horari amb ID " + horariId + " no s'ha trobat."));
        
        if("reservat".equals(horariDb.getEstat())) {
            throw new HorariReservatException("L'horari amb ID " + horariId + " ja està reservat per un altre grup.");
        }
        
        horariDb.setEstat("reservat");
        horariRepository.save(horariDb);
        return grupRepository.save(grup);
    }

    @Override
    @Transactional
    public void deleteGrup(Long id) throws GrupNotFoundException {
        Grup grup = grupRepository.findById(id)
                .orElseThrow(() -> new GrupNotFoundException("Grup amb ID " + id + " no trobat."));
        
        Horari horari = grup.getHorari();
        horari.setEstat("lliure");
        horariRepository.save(horari);
        grupRepository.deleteById(id);
    }

    @Override
    public List<Grup> findAllGrups() {
        return grupRepository.findAll();
    }
    
}
