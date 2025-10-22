/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.repository;

import com.bibliotecasedaos.biblioteca.entity.Exemplar;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dg
 */
@Repository
public interface ExemplarRepository extends JpaRepository<Exemplar, Long>{
    
    @Query("SELECT e FROM Exemplar e WHERE e.reservat = 'lliure'")
    List<Exemplar> findExemplarsLliures();
    
    @Query("SELECT e FROM Exemplar e JOIN e.llibre l WHERE e.reservat = 'lliure' AND LOWER(l.titol) LIKE LOWER(CONCAT('%', :titol, '%'))")
    List<Exemplar> findExemplarsLliuresByLlibreTitol(@Param("titol") String titol);
    
    @Query("SELECT e FROM Exemplar e JOIN e.llibre l JOIN l.autor a " +
           "WHERE e.reservat = 'lliure' AND LOWER(a.nom) LIKE LOWER(CONCAT('%', :nomAutor, '%'))")
    List<Exemplar> findExemplarsLliuresByAutorNom(@Param("nomAutor") String nomAutor);
}
