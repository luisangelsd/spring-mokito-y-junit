package com.mokitoyjunit.springmokitoyjunit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokitoyjunit.springmokitoyjunit.Datos;
import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//-- WebClient -> WebFLux (proyectos reactivos)
//-- WebTestClient: Consumimos nuestros endponds con WebClient simulando que otro servicio lo consume
//-- WebClient: Es para consumir api externas pero para Webflux / parecido a RestTemplate
//-- Levanta un servidor aparte al de la app para simular el consumo de nuestro endpoind
//-- Cuendo la API que vamos a consumir esta dentro del mismo proyecto no es necesario levantar el proyecto de spring (solo culocar la ruta base)
//-- Tienes que levantar primero tu proyecto de Spring
//-- Cuando testeamos de esta forma los metodos Test si modifican la BD - Ten cuidado

/*
    1) Añadir dependencia:
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
        <scope>test</scope>
    </dependency>
*/



//-- Levanta un servidor de prueba con puerto aleatorio
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Notas_ControllerCuentaWebClientTest_WebFlux {



    //-- Inyección de dependencia
    @Autowired
    private WebTestClient webTestClient;

    private ObjectMapper mapper = new ObjectMapper();
    private String url="http://localhost:8080/";


    //====================================================================================
    //-- Ejemplo Post(): Enviar y Recibir infomración + Validacion
    @Order(1)
    @Test
    void transferirDineroTest() throws JsonProcessingException {
        webTestClient.post().uri(url+"/cuenta/transferir")              //-- Url endpoind
                    .contentType(MediaType.APPLICATION_JSON)                                 //-- Request: Formato en el cual enviamos información
                    .bodyValue(Datos.getTransferencia1())                                    //-- Request: Enviamos los datos que necesita
                    .exchange()                                                             //--  Ejecutamos el Request
                    .expectStatus().isOk()                                                  //-- Response: Que es status sea 200
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)                 //-- Response: Comprobar que sea un JSON como respuesta
                    .expectBody()                                                                                             //-- Indicamos que queremos trabajar en el body
                    .jsonPath("$.mensaje").value( valor-> assertEquals("transferencia exitosa", valor))    //-- Response: Validamos por parametro en JSON con lambda + JUnit
                    .jsonPath("$.mensaje").value(Matchers.is("transferencia exitosa"))                       //-- Response: Validamos por parametro en JSON con Matchers
                     .jsonPath("$.mensaje").isEqualTo("transferencia exitosa")                       //-- Response: Validamos por parametro en JSON con isEqualTo
                    .json(mapper.writeValueAsString(Datos.getDtoResponse1()));                                              // Response: Validar por el JSON completo, para ello convertimos el objeto en JSON
    }



    //====================================================================================
    //-- Ejemplo Get(): Resibir información mapeando a un DTO + validaciones
   @Order(2)
    @Test
    void verUsuarioById1(){
        webTestClient.get().uri(url + "cuenta/ver/1") //-- Request: Url endpoind
                .exchange()                               //-- Request: Ejecutamos el Request
                .expectStatus().isOk()                    //-- Response: Que es status sea 200
                .expectHeader().contentType(MediaType.APPLICATION_JSON)     //-- Response: Comprobar que sea un JSON como respuesta
                .expectBody(DtoCuenta.class)                                //-- Response: Indicamos que queremos trabajar en el body y que estara mapeado a una clase
                .consumeWith(dto->{
                   DtoCuenta dtoCuenta=dto.getResponseBody();               //-- Response: Mapeamos la respuesta a un Object DTO
                    Assertions.assertEquals(1,dtoCuenta.getId());   //-- Response: Realizamos validaciónes
                    Assertions.assertEquals("persona 1",dtoCuenta.getPersona());
                    Assertions.assertEquals("1000.00",dtoCuenta.getSaldo().toPlainString());
                });
    }


    //====================================================================================
    //-- Ejemplo Get: Recibir información .jsonPath + validaciones
    @Order(3)
    @Test
    void verUsuarioById2() throws JsonProcessingException {

        webTestClient.get().uri(url+"/cuenta/ver/2")        //-- Request: Url endpoind
                .exchange()                                     //-- Request: Ejecutamos el Request
                .expectStatus().isOk()                          //-- Response: Que es status sea 200
                .expectHeader().contentType(MediaType.APPLICATION_JSON) //-- Response: Comprobar que sea un JSON como respuesta
                .expectBody()                                           //-- Response: Indicamos que queremos trabajar en el JSON por defecto
                .jsonPath("$.id").isEqualTo(2)     //-- Response: Realizamos validaciónes
                .jsonPath("$.persona").isEqualTo("persona 2")
                .jsonPath("$.saldo").isEqualTo(new BigDecimal("2000.0"));
    }


    //====================================================================================
    //-- Ejemplo Get: Recibir array información .jsonPath + validaciones

    @Order(4)
    @Test
    void findAllUsersTest(){
        webTestClient.get().uri(url+"/cuenta/ver/all")      //-- Request: Url endpoind
                .exchange()                                     //-- Request: Ejecutamos el Request
                .expectStatus().isOk()                          //-- Response: Que es status sea 200
                .expectHeader().contentType(MediaType.APPLICATION_JSON)     //-- Response: Comprobar que sea un JSON como respuesta
                .expectBody()                                               //-- Response: Indicamos que queremos trabajar en el JSON por defecto
                .jsonPath("$[0].id").value( v -> assertEquals(1,v))      //-- Response: Realizamos validaciónes
                .jsonPath("$[0].persona").isEqualTo("persona 1")
                .jsonPath("$[0].saldo").value(Matchers.hasToString("1000.0"))
                .jsonPath("$").isArray()                                //-- Response: Validamos que sea un array
                .jsonPath("$").value(Matchers.hasSize(2));              //-- Response: Validamos que conteng un tamaño de 2
    }


    //====================================================================================
    //-- Ejemplo Get: Recibir array información mapeando  + validaciones

    @Order(4)
    @Test
    void findAllUsersTestMapeado(){
        webTestClient.get().uri(url+"/cuenta/ver/all")          //-- Request: Url endpoind
                .exchange()                                         //-- Request: Ejecutamos el Request
                .expectStatus().isOk()                              //-- Response: Que es status sea 200
                .expectHeader().contentType(MediaType.APPLICATION_JSON)     //-- Response: Comprobar que sea un JSON como respuesta
                .expectBodyList( DtoCuenta.class)                           //-- Response: Indicamos que queremos trabajar en el body y que estara mapeado a una list de objetos
                .consumeWith(dto -> {

                    List<DtoCuenta> list= dto.getResponseBody();                    //-- Response: Mapeamos la respuesta a una list de  Object DTO
                    Assertions.assertEquals(1,      list.get(0).getId());   //-- Response: Realizamos validaciónes
                    Assertions.assertEquals("persona 1",  list.get(0).getPersona());
                    Assertions.assertEquals("1000.00",  list.get(0).getSaldo().toPlainString());
                    Assertions.assertNotNull(list);
                });
    }




}