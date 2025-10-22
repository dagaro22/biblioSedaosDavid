/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.error.AutorNotFoundException;
import java.util.List;

/**
 *
 * @author dg
 */
public interface AutorService {
    
    List<Autor> findAllAutors();
    Autor saveAutor(Autor autor);
    void deleteAutor(long id) throws AutorNotFoundException;
}
