/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "exemplars")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Exemplar {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exemplars_seq")
    @SequenceGenerator(name = "exemplars_seq", sequenceName = "exemplars_id_seq", allocationSize = 1)
    private Long id;
    
    private String lloc;
    private String reservat;
    
    @ManyToOne
    @JoinColumn(name = "llibre_id", referencedColumnName = "id")
    private Llibre llibre;
}
