/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entitat que representa un Grup de lectura i els seus membres a la base de dades.
 * 
 * @author David García Rodríguez
 */
@Entity
@Table(name = "grups")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Grup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grups_seq")
    @SequenceGenerator(name = "grups_seq", sequenceName = "grups_id_seq", allocationSize = 1)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String nom;
    @Column(nullable = false)
    private String tematica;
    
    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private Usuari administrador;
    
    @OneToOne
    @JoinColumn(name = "horari_id", referencedColumnName = "id")
    private Horari horari;
    
    @ManyToMany
            @JoinTable(name = "grup_usuari_map",
                    joinColumns = @JoinColumn(name = "grup_id", referencedColumnName = "id"),
                    inverseJoinColumns = @JoinColumn(name = "usuari_id", referencedColumnName = "id"))
    List<Usuari> membres;
       
}
