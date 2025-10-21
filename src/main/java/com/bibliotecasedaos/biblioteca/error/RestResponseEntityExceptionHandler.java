/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.error;

import com.bibliotecasedaos.biblioteca.error.dto.ErrorMessage;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author dg
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
    
    //Maneja UsuariNotFoundException (404 Not Found)
    @ExceptionHandler(UsuariNotFoundException.class)
    public ResponseEntity<ErrorMessage> usuariNotFoundException(UsuariNotFoundException exception) {
        // Simplificat per consistència
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    //Maneja els errors de validació de camp (@Valid)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String,Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{
            errors.put(error.getField(), error.getDefaultMessage());
        });
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    
    //Maneja errors de negoci (Nick duplicat)
    @ExceptionHandler(NickAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleNickAlreadyExistsException(NickAlreadyExistsException ex) {
        ErrorMessage message = new ErrorMessage(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    
    //Maneja fallades de validació de JPA/Hibernate (Ex: @Size, @NotNull) a la capa de persistència (400 Bad Request)
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(
        jakarta.validation.ConstraintViolationException ex) {
        
        Map<String, String> errors = new HashMap<>();
        
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    // Maneja errors d'integritat de la BBDD (Clau duplicada, etc.) (409 Conflict)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        
        String errorMessage = "S'ha violat una restricció de dades (clau duplicada o valor nul). ";
        
        if (ex.getCause() != null && ex.getCause().getMessage().contains("llave duplicada")) {
            errorMessage += "Detall: " + ex.getCause().getMessage();
        } else {
            errorMessage += "Si us plau, revisa si el Nick, NIF o Email ja existeixen.";
        }

        ErrorMessage message = new ErrorMessage(HttpStatus.CONFLICT, errorMessage);
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    
}
