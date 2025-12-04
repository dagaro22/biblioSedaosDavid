/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.error;

/**
 * Excepció personalitzada llançada quan es supera el nombre de membres d'un grup.
 * 
 * @author David García Rodríguez
 */
public class LimitDeMembresSuperatException extends Exception{
    public LimitDeMembresSuperatException(String message) {
        super(message);
    }
}
