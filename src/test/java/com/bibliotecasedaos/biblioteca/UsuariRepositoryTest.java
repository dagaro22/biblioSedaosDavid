/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bibliotecasedaos.biblioteca;

import com.bibliotecasedaos.biblioteca.entity.Usuari;
import com.bibliotecasedaos.biblioteca.entity.Role;
import com.bibliotecasedaos.biblioteca.repository.UsuariRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

/**
 * Test per verificar el trovar usuari per nick. Prova la capa de repositori del usuari.
 * 
 * @author David García Rodríguez
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UsuariRepositoryTest {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private TestEntityManager entityManager; 

    private Usuari mockUser;

    @BeforeEach
    void setUp() {

        mockUser = Usuari.builder()
                .nick("car")
                .nif("111111111")
                .nom("Carlos")
                .cognom1("Cognom1")
                .cognom2("Cognom2")
                .email("carlos@gmail.com")
                .password("qwerty") 
                .tlf("666666667")
                .carrer("C/carrer")
                .provincia("Bcn")
                .localitat("localitat")
                .cp("08660")
                .rol(1)
                .build();
        
        entityManager.persistAndFlush(mockUser);
    } 

    /**
     * Test que verifica la devolució d'un usuari pel seu nick.
     */
    @Test
    void findUsuariByNickWithJPQLExistTest() {

        Optional<Usuari> foundUser = usuariRepository.findUsuariByNickWithJPQL("car");

        assertThat(foundUser).isPresent(); 
        assertThat(foundUser.get().getNick()).isEqualTo("car");
    }
    

    /**
     * Test que retorna un optional null si no troba l'usuari pel nick.
     */
    @Test
    void findByNifWithBadNickTest() {

        Optional<Usuari> foundUser = usuariRepository.findUsuariByNickWithJPQL("xxxxxx");

        assertThat(foundUser).isEmpty();
    }
}
