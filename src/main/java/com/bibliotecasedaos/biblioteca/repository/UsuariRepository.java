/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.repository;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dg
 */
@Repository
public interface UsuariRepository extends JpaRepository<Usuari, Long>{
    
    //Consulta con JPQL
    @Query("SELECT u FROM Usuari u WHERE u.nick = :nick")
    Optional<Usuari> findUsuariByNickWithJPQL(String nick);
    
    //Consulta con Inversión de control
    Optional<Usuari> findByNif(String nif);
}
