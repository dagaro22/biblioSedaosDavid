/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author dg
 */
/**
 * Entitat que representa un usuari en la base de dades de la biblioteca.
 * Aquesta classe implementa {@code UserDetails} per a la integració amb
 * Spring Security, proporcionant la informació necessària per a
 * l'autenticació i l'autorització.
 *
 * @author David García Rodríguez
 */
@Entity
@Table(name = "usuaris")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuari implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Per favor afegeix un nick")
    private String nick;
    //@Length(min = 9, max = 9)
    private String nif;
    private String nom;
    private String cognom1;
    private String cognom2;
    private String localitat;
    private String provincia;
    private String carrer;
    private String cp;
    private String tlf;
    private String email;
    private String password;
    private int rol;
    
    /*
    @Enumerated(EnumType.ORDINAL)
    private Role role;
*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        String roleName = "USER";
        if (this.rol == 2) {
            roleName = "ADMIN";
        }
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getUsername() {
        return nick;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }
    
    
}
