/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 *
 * @author dg
 */
@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class NickAlreadyExistsException extends RuntimeException {
    /**
     * Excepció llançada quan es detecta que el Nick d'usuari ja existeix.
     */
    public NickAlreadyExistsException(String message) {
        super(message);
    }

}
