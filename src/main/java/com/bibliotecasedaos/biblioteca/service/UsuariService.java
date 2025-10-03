/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author dg
 */
public interface UsuariService {
    
    List<Usuari> findAllUsuaris();
    Usuari saveUsuari(Usuari usauri);
    Usuari updateUsuari(Long id, Usuari usuari);
    void deleteUsuari(Long id);
    Optional<Usuari> findUsuariByNameWithJPQL(String nick);
    Optional<Usuari> findByNif(String nif);
}
