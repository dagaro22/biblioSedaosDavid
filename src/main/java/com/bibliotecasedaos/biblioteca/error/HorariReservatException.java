/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.error;

/**
 * Excepció personalitzada llançada quan s'intenta realitzar una reserva amb un horari que es troba en un estat "reservat".
 * 
 * @author David García Rodríguez
 */
public class HorariReservatException extends Exception{
    public HorariReservatException(String message) {
        super(message);
    }
}
