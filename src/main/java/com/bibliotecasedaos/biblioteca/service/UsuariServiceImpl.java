/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.UsuariNotFoundException;
import com.bibliotecasedaos.biblioteca.repository.UsuariRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementació del servei de lògica de negoci per a la gestió d'usuaris.
 * Aquesta classe actua com a pont entre el controlador i la capa de persistència
 * (Repositori), contenint la lògica per a la validació, cerca i modificació de dades d'usuaris.
 * 
 * @author dg
 */
@Service
public class UsuariServiceImpl implements UsuariService {

    @Autowired
    UsuariRepository usuariRepository;
    
    /**
     * Recupera una llista de tots els usuaris emmagatzemats a la base de dades.
     *
     * @return Una llista de totes les entitats {@code Usuari}.
     */
    @Override
    public List<Usuari> findAllUsuaris() {
        return usuariRepository.findAll();
    }

    /**
     * Guarda una nova entitat d'usuari a la base de dades.
     * Utilitzat principalment per a la creació d'un nou usuari i actualitzacions. 
     *
     * @param usuari L'entitat {@code Usuari} a guardar.
     * @return L'entitat {@code Usuari} guardada amb possibles modificacions (ex. ID assignat).
     */
    @Override
    public Usuari saveUsuari(Usuari usuari) {
        return usuariRepository.save(usuari);
    }

    /**
     * Actualitza els camps d'un usuari existent basant-se en l'ID proporcionat.
     * Només s'actualitzen els camps que no són nuls o buits en l'entitat {@code usuari}
     * de la petició.
     *
     * @param id L'identificador de l'usuari a actualitzar.
     * @param usuari L'entitat {@code Usuari} amb els nous valors a aplicar.
     * @return L'entitat {@code Usuari} amb les dades actualitzades.
     * @throws UsuariNotFoundException Si no es troba cap usuari amb l'ID donat.
     */
    @Override
    public Usuari updateUsuari(Long id, Usuari usuari) throws UsuariNotFoundException{
        Usuari usuariDb = usuariRepository.findById(id)
            .orElseThrow(() -> new UsuariNotFoundException("Usuari amb ID " + id + " no trobat."));
        
        if (Integer.valueOf(usuari.getRol()) != null && usuari.getRol() >= 0 && usuari.getRol() <= 1) {
            usuariDb.setRol(usuari.getRol());
        }
        if (Objects.nonNull(usuari.getCarrer()) && !usuari.getCarrer().isBlank()) {
            usuariDb.setCarrer(usuari.getCarrer());
        }
        if (Objects.nonNull(usuari.getCognom1()) && !"".equalsIgnoreCase(usuari.getCognom1())) {
            usuariDb.setCognom1(usuari.getCognom1());
        }            
        if (Objects.nonNull(usuari.getCognom2()) && !"".equalsIgnoreCase(usuari.getCognom2())) {
            usuariDb.setCognom2(usuari.getCognom2());
        }
        if (Objects.nonNull(usuari.getCp()) && !"".equalsIgnoreCase(usuari.getCp())) {
            usuariDb.setCp(usuari.getCp());
        }
        if (Objects.nonNull(usuari.getEmail()) && !"".equalsIgnoreCase(usuari.getEmail())) {
            usuariDb.setEmail(usuari.getEmail());
        }
        if (Objects.nonNull(usuari.getLocalitat()) && !"".equalsIgnoreCase(usuari.getLocalitat())) {
            usuariDb.setLocalitat(usuari.getLocalitat());
        }
        if (Objects.nonNull(usuari.getNick()) && !"".equalsIgnoreCase(usuari.getNick())) {
            usuariDb.setNick(usuari.getNick());
        }
        if (Objects.nonNull(usuari.getNif()) && !"".equalsIgnoreCase(usuari.getNif())) {
            usuariDb.setNif(usuari.getNif());
        }
        if (Objects.nonNull(usuari.getNom()) && !"".equalsIgnoreCase(usuari.getNom())) {
            usuariDb.setNom(usuari.getNom());
        }
        if (Objects.nonNull(usuari.getPassword()) && !"".equalsIgnoreCase(usuari.getPassword())) {
            usuariDb.setPassword(usuari.getPassword());
        }
        if (Objects.nonNull(usuari.getProvincia()) && !"".equalsIgnoreCase(usuari.getProvincia())) {
            usuariDb.setProvincia(usuari.getProvincia());
        }
        if (Objects.nonNull(usuari.getTlf()) && !"".equalsIgnoreCase(usuari.getTlf())) {
            usuariDb.setTlf(usuari.getTlf());
        }
        
        return usuariRepository.save(usuariDb);
        
    }

    /**
     * Elimina un usuari de la base de dades mitjançant el seu identificador.
     *
     * @param id L'identificador de l'usuari a eliminar.
     */
    @Override
    public void deleteUsuari(Long id) {
        usuariRepository.deleteById(id);
    }

    /**
     * Cerca un usuari pel seu nom d'usuari (nick).
     *
     * @param nick El nom d'usuari a cercar.
     * @return Un {@link Optional} que conté l'usuari trobat.
     * @throws UsuariNotFoundException Si l'usuari no es troba pel nick.
     */
    @Override
    public Optional<Usuari> findUsuariByNameWithJPQL(String nick) throws UsuariNotFoundException {
        Optional<Usuari> usuari =  usuariRepository.findUsuariByNickWithJPQL(nick);
        if (!usuari.isPresent()) {
            throw new UsuariNotFoundException("Usuari no trobat per nick.");
        }
        return usuari;
    }

    @Override
    public Optional<Usuari> findByNif(String nif) {
        return usuariRepository.findByNif(nif);
    }

    /**
     * Cerca un usuari pel seu identificador (ID).
     *
     * @param id L'identificador de l'usuari a cercar.
     * @return L'entitat {@code Usuari} trobada.
     * @throws UsuariNotFoundException Si no es troba cap usuari amb l'ID donat.
     */
    @Override
    public Usuari findUsuariById(Long id) throws UsuariNotFoundException{
        Optional<Usuari> usuari = usuariRepository.findById(id);
        if (!usuari.isPresent()) {
            throw new UsuariNotFoundException("Usuari no trobat.");
        }
        return usuari.get();
    }

    @Override
    public Optional<Usuari> findUsuariByNifWithJPQL(String nif) {
        return usuariRepository.findUsuariByNifWithJPQL(nif);
    }
    
}
