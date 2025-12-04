/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Horari;
import java.util.List;

/**
 * Interfície de servei per a la gestió d'horaris a l'aplicació.
 * Defineix les operacions bàsiques de CRUD
 * 
 * @author David García Rodríguez
 */
public interface HorariService {
    
    /**
     * Recupera una llista amb tots els objectes {@code Horari} existents.
     *
     * @return Una llista de tots els horaris, pot ser una llista buida si no hi ha cap horari.
     */
    List<Horari> findAllHoraris();
    
    /**
     * Desa un nou objecte {@code Horari} a la base de dades o actualitza un existent.
     *
     * @param horari L'objecte Horari que es vol desar.
     * @return L'objecte Horari desat, incloent-hi l'identificador (ID) assignat.
     */
    Horari saveHorari(Horari horari);
}
