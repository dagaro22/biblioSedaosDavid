/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.service;

import com.bibliotecasedaos.biblioteca.config.JwtService;
import com.bibliotecasedaos.biblioteca.controller.models.AuthResponse;
import com.bibliotecasedaos.biblioteca.controller.models.AuthenticationRequest;
import com.bibliotecasedaos.biblioteca.controller.models.RegisterRequest;
import com.bibliotecasedaos.biblioteca.entity.Role;
import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.repository.UsuariRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author dg
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UsuariRepository usuariRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @Override
    public AuthResponse register(RegisterRequest request) {
        
        var user = Usuari.builder()
                .nick(request.getNick())
                .nom(request.getNom())
                .cognom1(request.getCognom1())
                .cognom2(request.getCognom2())
                .localitat(request.getLocalitat())
                .provincia(request.getProvincia())
                .carrer(request.getCarrer())
                .cp(request.getCp())
                .tlf(request.getTlf())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .role(Role.USER)
                .build();
        usuariRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken).build();
    }

    @Override
    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getNick(), 
                request.getPassword())
        );
        
        var user = usuariRepository.findUsuariByNickWithJPQL(request.getNick()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .nom(user.getNom())
                .cognom1(user.getCognom1())
                .cognom2(user.getCognom2())
                .rol(user.getRol())
                .role(user.getRole())
                .id(user.getId())
                .build();
        
    }
    
    
    
}
