/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca.config;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * 
 * Filtre de seguretat per processar JSON Web Tokens (JWT).
 * Extreu i valida el token de la capçalera 'Authorization' de les peticions al servidor.
 * Si el token és vàlid, autentica l'usuari.
 * 
 * @author David García Rodríguez
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

    /** Servei per carregar els detalls de l'usuari.*/
    private final UserDetailsService userDetailService;
    /** Servei per fer la gestiódels JWT.*/
    private final JwtService jwtService;
    //Nuevo------------------------------
    private final TokenBlacklist tokenBlacklist;
    
    /**
     * David García Rodríguez
     * Extreu i valida el token de la capçalera 'Authorization' de les peticions al servidor.
     * Si el token és vàlid, autentica l'usuari.
     * * @param (httpServletRequest).
     * @param request (httpServletResponse).
     * @param response (filterChain).
     * @param filterChain
     * @throws ServletException Si una excepció específica de servlet ocorre.
     * @throws IOException Si ocorre un error d'entrada/sortida.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userNick;
        
        
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        
        // Extracció i comprovació de la llista negra
        try {
            Claims claims = jwtService.extractAllClaims(jwt);
            String tokenId = claims.getId(); 

            if (tokenBlacklist.isTokenBlacklisted(tokenId)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token revocat (Logout previ).");
                return;
            }

            userNick = jwtService.getUserName(jwt); 
        } catch (Exception e) {

            filterChain.doFilter(request, response);
            return;
        }
        
    try{    
        //jwt = authHeader.substring(7);
        //userNick = jwtService.getUserName(jwt);
        
        /**Comprova que l'usuari existeix i no hi ha autenticació prèvia, en cas exitos crea i estableix el token_seguretat*/
        if (userNick != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailService.loadUserByUsername(userNick);
           
            if (jwtService.validateToken(jwt, userDetails)) {
                
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
        filterChain.doFilter((request), response);
        
    }
    
}
