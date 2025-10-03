/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author dg
 */
@Entity
@Table(name = "usuaris")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuari {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String nick;
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
    
    
}
