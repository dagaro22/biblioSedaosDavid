/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.UsuariNotFoundException;
import java.util.List;
import java.util.Optional;


/**
 * Interfície de servei per gestionar les diferents operacions sobre la entitat 'Usuari'
 * 
 * @author David García Rodríguez
 */
public interface UsuariService {
    
    /**
     * Obté una llista amb tots els usuaris en la base de dades.
     * @return Una {@code List} de tots els {@link Usuari}.
     */
    List<Usuari> findAllUsuaris();
    
    Usuari saveUsuari(Usuari usauri);
    Usuari updateUsuari(Long id, Usuari usuari);
    void deleteUsuari(Long id);
    Optional<Usuari> findUsuariByNameWithJPQL(String nick);
    Optional<Usuari> findUsuariByNifWithJPQL(String nif);
    Optional<Usuari> findByNif(String nif);
    Usuari findUsuariById(Long id) throws UsuariNotFoundException;
}
