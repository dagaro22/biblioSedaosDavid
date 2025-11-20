/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Grup;
import com.bibliotecasedaos.biblioteca.entity.Horari;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.GrupNotFoundException;
import com.bibliotecasedaos.biblioteca.error.HorariNotFoundException;
import com.bibliotecasedaos.biblioteca.error.HorariReservatException;
import com.bibliotecasedaos.biblioteca.error.LimitDeMembresSuperatException;
import com.bibliotecasedaos.biblioteca.error.MembreJaExisteixException;
import com.bibliotecasedaos.biblioteca.error.UsuariNotFoundException;
import com.bibliotecasedaos.biblioteca.repository.GrupRepository;
import com.bibliotecasedaos.biblioteca.repository.HorariRepository;
import com.bibliotecasedaos.biblioteca.repository.UsuariRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
    
    @Autowired
    private UsuariRepository usuariRepository;
    
    private static final int MAXIM_MEMBRES = 5;
    
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

    @Override
    public Grup afegirUsuariGrup(Long grupId, Long membreId) throws GrupNotFoundException, UsuariNotFoundException, LimitDeMembresSuperatException, MembreJaExisteixException {
        
        Grup grup = grupRepository.findById(grupId)
                .orElseThrow(()-> new GrupNotFoundException("Grup amb ID " + grupId + " no trobat."));
        
        Usuari nouMembre = usuariRepository.findById(membreId)
                .orElseThrow(() -> new UsuariNotFoundException("Usuari amb ID " + membreId + " no trobat."));
        
        if (grup.getMembres().size() >= MAXIM_MEMBRES) {
            throw new LimitDeMembresSuperatException("El grup " + grup.getNom() + " ja té " + MAXIM_MEMBRES + " membres.");
        }
        
        Optional<Usuari> membreExistent = grup.getMembres().stream()
                .filter(u -> u.getId().equals(membreId))
                .findFirst();
        
        if (membreExistent.isPresent()) {
            throw new MembreJaExisteixException("L'usuari ja és membre del grup");
        }
        
        grup.getMembres().add(nouMembre);    
        return grupRepository.save(grup);
    }
    
}
