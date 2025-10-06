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
 *
 * @author dg
 */
@Service
public class UsuariServiceImpl implements UsuariService{

    @Autowired
    UsuariRepository usuariRepository;
    
    @Override
    public List<Usuari> findAllUsuaris() {
        return usuariRepository.findAll();
    }

    @Override
    public Usuari saveUsuari(Usuari usuari) {
        return usuariRepository.save(usuari);
    }

    @Override
    public Usuari updateUsuari(Long id, Usuari usuari) {
        Usuari usuariDb = usuariRepository.findById(id).get();
        
        if (Integer.valueOf(usuari.getRol()) != null && usuari.getRol() >= 0 && usuari.getRol() <= 1) {
            usuariDb.setRol(usuari.getRol());
        }
        if (Objects.nonNull(usuari.getCarrer()) && !"".equalsIgnoreCase(usuari.getCarrer())) {
            usuariDb.setNif(usuari.getNif());
        }
        if (Objects.nonNull(usuari.getCognom1()) && !"".equalsIgnoreCase(usuari.getCognom1())) {
            usuariDb.setNif(usuari.getCognom1());
        }            
        if (Objects.nonNull(usuari.getCognom2()) && !"".equalsIgnoreCase(usuari.getCognom2())) {
            usuariDb.setNif(usuari.getCognom2());
        }
        if (Objects.nonNull(usuari.getCp()) && !"".equalsIgnoreCase(usuari.getCp())) {
            usuariDb.setNif(usuari.getCp());
        }
        if (Objects.nonNull(usuari.getEmail()) && !"".equalsIgnoreCase(usuari.getEmail())) {
            usuariDb.setNif(usuari.getEmail());
        }
        if (Objects.nonNull(usuari.getLocalitat()) && !"".equalsIgnoreCase(usuari.getLocalitat())) {
            usuariDb.setNif(usuari.getLocalitat());
        }
        if (Objects.nonNull(usuari.getNick()) && !"".equalsIgnoreCase(usuari.getNick())) {
            usuariDb.setNif(usuari.getNick());
        }
        if (Objects.nonNull(usuari.getNif()) && !"".equalsIgnoreCase(usuari.getNif())) {
            usuariDb.setNif(usuari.getNif());
        }
        if (Objects.nonNull(usuari.getNom()) && !"".equalsIgnoreCase(usuari.getNom())) {
            usuariDb.setNif(usuari.getNom());
        }
        if (Objects.nonNull(usuari.getPassword()) && !"".equalsIgnoreCase(usuari.getPassword())) {
            usuariDb.setNif(usuari.getPassword());
        }
        if (Objects.nonNull(usuari.getProvincia()) && !"".equalsIgnoreCase(usuari.getProvincia())) {
            usuariDb.setNif(usuari.getProvincia());
        }
        if (Objects.nonNull(usuari.getTlf()) && !"".equalsIgnoreCase(usuari.getTlf())) {
            usuariDb.setNif(usuari.getTlf());
        }
        
        return usuariRepository.save(usuariDb);
        
    }

    @Override
    public void deleteUsuari(Long id) {
        usuariRepository.deleteById(id);
    }

    @Override
    public Optional<Usuari> findUsuariByNameWithJPQL(String nick) {
        return usuariRepository.findUsuariByNickWithJPQL(nick);
    }

    @Override
    public Optional<Usuari> findByNif(String nif) {
        return usuariRepository.findByNif(nif);
    }

    @Override
    public Usuari findUsuariById(Long id) throws UsuariNotFoundException{
        Optional<Usuari> usuari = usuariRepository.findById(id);
        if (!usuari.isPresent()) {
            throw new UsuariNotFoundException("Usuari no trobat.");
        }
        return usuari.get();
    }
    
}
