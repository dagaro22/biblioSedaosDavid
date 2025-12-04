/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.error;

/**
 * Excepció d'execució (Exception) llançada quan s'intenta afegir un membre que
 * ja existeix a la base de dades.
 * 
 * @author David García Rodríguez
 */
public class MembreJaExisteixException extends Exception{
    
    /**
     * Excepció llançada quan es detecta que un membre ja existeix.
     * @param message
     */
    public MembreJaExisteixException(String message) {
        super(message);
    }
}
