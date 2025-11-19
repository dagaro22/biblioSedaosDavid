/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author David García Rodríguez
 */
@Entity
@Table(name = "horaris", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sala", "dia", "hora"}, name = "UK_HORARI_SALA_DIA_HORA") 
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Horari {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "horaris_seq")
    @SequenceGenerator(name = "horaris_seq", sequenceName = "horaris_id_seq", allocationSize = 1)
    private Long id;
    
    @Column(nullable = false)
    private String sala;
    @Column(nullable = false)
    private String dia;
    @Column(nullable = false)
    private String hora;
    //reservat o lliure
    private String estat;
    
}
