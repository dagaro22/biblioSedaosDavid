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
@Table(name = "llibres")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Llibre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "llibres_seq")
    @SequenceGenerator(name = "llibres_seq", sequenceName = "llibres_id_seq", allocationSize = 1)
    private Long id;
    
    private String isbn;
    private String titol;
    private int pagines;
    private String editorial;
    
    @ManyToOne
    @JoinColumn(name = "autor_id", referencedColumnName = "id")
    private Autor autor;
}
