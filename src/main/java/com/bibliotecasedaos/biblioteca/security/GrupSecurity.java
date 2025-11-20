/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.security;

import com.bibliotecasedaos.biblioteca.entity.Grup;
import com.bibliotecasedaos.biblioteca.repository.GrupRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author David García Rodríguez
 */
@Component("grupSecurity")
public class GrupSecurity {
    
    @Autowired
    private GrupRepository grupRepository;

    public boolean esAdminDelGrup(Long grupId, Long userId) {
        
        Optional<Grup> grup = grupRepository.findById(grupId);
        
        if (grup.isEmpty() || grup.get().getAdministrador() == null) {
            return false;
        }

        return grup.get().getAdministrador().getId().equals(userId);
    }
    
}
