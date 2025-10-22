/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.entity.Autor;
import com.bibliotecasedaos.biblioteca.error.AutorNotFoundException;
import com.bibliotecasedaos.biblioteca.repository.AutorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dg
 */
@Service
public class AutorServiceImpl implements AutorService{

    @Autowired
    AutorRepository autorRepository;
    
    @Override
    public List<Autor> findAllAutors() {
        return autorRepository.findAllByOrderByNomAsc();
    }

    @Override
    public Autor saveAutor(Autor autor) {
        return  autorRepository.save(autor);
    }

    @Override
    public void deleteAutor(long id) throws AutorNotFoundException{
        if (!autorRepository.findById(id).isPresent()) {
            throw new AutorNotFoundException("Autor no trobat.");
        } else {
            autorRepository.deleteById(id);
        }
               
    }
    
}
