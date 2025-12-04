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
 * Classe que proporciona la lògica de negoci per a la gestió dels grups, implementant les operacions de cerca i manteniment de dades.
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
    
    /**
     * Desa un nou grup i s'assegura de marcar l'horari associat com a 'reservat'.
     * @param grup L'objecte Grup a desar.
     * @return El grup desat amb el seu identificador.
     * @throws HorariNotFoundException Si l'ID de l'horari dins del grup no correspon a cap horari existent.
     * @throws HorariReservatException Si l'horari ja es troba en estat 'reservat' per un altre grup.
     */
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

    /**
     * Elimina un grup per ID i allibera l'horari associat marcant-lo com a 'lliure'.
     * @param id L'identificador del grup a eliminar.
     * @throws GrupNotFoundException Si no es troba cap grup amb l'ID proporcionat.
     */
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

    /**
     * Recupera una llista de tots els grups existents.
     * @return Una llista d'objectes Grup.
     */
    @Override
    public List<Grup> findAllGrups() {
        return grupRepository.findAll();
    }

    /**
     * Afegeix un usuari a un grup existent després de realitzar diverses validacions:
     * @param grupId L'identificador del grup al qual s'afegeix el membre.
     * @param membreId L'identificador de l'usuari que es vol afegir.
     * @return El grup actualitzat amb el nou membre.
     * @throws GrupNotFoundException Si no es troba el grup.
     * @throws UsuariNotFoundException Si no es troba l'usuari que es vol afegir.
     * @throws LimitDeMembresSuperatException Si el grup ja ha arribat al nombre màxim de membres.
     * @throws MembreJaExisteixException Si l'usuari ja forma part del grup.
     */
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

    /**
     * Recupera la llista de tots els usuaris que són membres d'un grup específic.
     * @param grupId L'identificador del grup del qual es volen obtenir els membres.
     * @return Una llista d'objectes Usuari que són membres del grup.
     * @throws GrupNotFoundException Si no es troba cap grup amb l'ID proporcionat.
     */
    @Override
    public List<Usuari> trobarMemebresGrup(Long grupId) throws GrupNotFoundException {
        Grup grup = grupRepository.findById(grupId)
                .orElseThrow(() -> new GrupNotFoundException("Grup amb ID " + grupId + " no trobat."));
        
        return grup.getMembres();
    }
    
    /**
     * Elimina un usuari de la llista de membres d'un grup.
     * @param grupId L'identificador del grup.
     * @param membreId L'identificador de l'usuari a eliminar.
     * @throws GrupNotFoundException Si no es troba el grup.
     * @throws UsuariNotFoundException Si l'usuari no es troba entre els membres del grup.
     */
    @Override
    @Transactional
    public void eliminarUsuariDeGrup(Long grupId, Long membreId) throws GrupNotFoundException, UsuariNotFoundException{
        Grup grup = grupRepository.findById(grupId)
                .orElseThrow(()-> new GrupNotFoundException("Grup amb ID " + grupId + " no trobat."));
        
        boolean membreEliminat = grup.getMembres().removeIf(usuari -> usuari.getId().equals(membreId));
        if (!membreEliminat) {
            throw new UsuariNotFoundException("L'usuari amb id " + membreId + " no s'ha trobat al grup amb id " + grupId);
        }
        
        grupRepository.save(grup);
    }
    
}
