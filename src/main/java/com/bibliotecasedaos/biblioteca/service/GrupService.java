/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Grup;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.error.GrupNotFoundException;
import com.bibliotecasedaos.biblioteca.error.HorariNotFoundException;
import com.bibliotecasedaos.biblioteca.error.HorariReservatException;
import com.bibliotecasedaos.biblioteca.error.LimitDeMembresSuperatException;
import com.bibliotecasedaos.biblioteca.error.MembreJaExisteixException;
import com.bibliotecasedaos.biblioteca.error.UsuariNotFoundException;
import java.util.List;

/**
 *
 * @author David García Rodríguez
 */
public interface GrupService {
    
    Grup saveGrup(Grup prestec) throws HorariNotFoundException, HorariReservatException;
    void deleteGrup(Long id) throws GrupNotFoundException;
    List<Grup> findAllGrups();
    
    Grup afegirUsuariGrup(Long grupId, Long membreId) throws GrupNotFoundException, UsuariNotFoundException, LimitDeMembresSuperatException, MembreJaExisteixException;
    List<Usuari> trobarMemebresGrup(Long grupId) throws GrupNotFoundException;
    void eliminarUsuariDeGrup(Long grupId, Long membreId) throws GrupNotFoundException, UsuariNotFoundException;
}
