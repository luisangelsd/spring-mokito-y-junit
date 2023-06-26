package com.mokitoyjunit.springmokitoyjunit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class  addControllerCuentaTest_ResTemplate_OK {

    @Autowired
    private TestRestTemplate  testRestTemplate;

    private ObjectMapper objectMapper=new ObjectMapper();
    private String url="http://localhost:8080";


    //------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("saveCuenta() - Exito")
    @Order(1)
    void saveCuenta() {
        //-- Datos
        DtoCuenta dtoSave=new DtoCuenta("persona 3", new BigDecimal("3000"));

        //-- Consumimos endPoind: url + RequestBody + Convertimos a String
        ResponseEntity<DtoCuenta> response = this.testRestTemplate.postForEntity("/cuenta/save",dtoSave, DtoCuenta.class);

        //-- Realizamos pruebas:
        Assertions.assertEquals("persona 3", response.getBody().getPersona());
        Assertions.assertEquals("3000", response.getBody().getSaldo().toPlainString());
        Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }


    @Test
    @DisplayName("saveCuenta() - BadRequest")
    @Order(2)
    void saveCuenta2() {

        //-- Datos
        DtoCuenta dtoSave=new DtoCuenta(null, new BigDecimal("3000"));

        //-- Servicio
        ResponseEntity<DtoCuenta> response = this.testRestTemplate.postForEntity("/cuenta/save", dtoSave, DtoCuenta.class);

        //-- Realizamos Test
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }




    //------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("updateCuenta() - Exito")
    @Order(3)
    void updateCuenta() {


    }

    @Test
    @DisplayName("updateCuenta() - BadRequest")
    @Order(4)
    void updateCuenta2() {


    }

    //------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("verUsuarioById() - Exito")
    @Order(3)
    void verUsuarioById() {

        //-- Servicio
        ResponseEntity<DtoCuenta> response=this.testRestTemplate.getForEntity("/cuenta/ver/1", DtoCuenta.class);
        System.out.println(response.getStatusCode());

        //-- Test
        Assertions.assertEquals(1, response.getBody().getId());
        Assertions.assertEquals("persona 1", response.getBody().getPersona());
        Assertions.assertEquals("1000.00", response.getBody().getSaldo().toPlainString());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

    }

    @Test
    @DisplayName("verUsuarioById() - NotFound")
    @Order(3)
    void verUsuarioById2() {

        //-- Servicio
        ResponseEntity<DtoCuenta> response=this.testRestTemplate.getForEntity("/cuenta/ver/400", DtoCuenta.class);

        //-- Test
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }



    //------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("findAllUsers() - Encuentra Datos")
    @Order(4)
    void findAllUsers() {

        //-- Servicio
        ResponseEntity<DtoCuenta[]> response= this.testRestTemplate.getForEntity("/cuenta/ver/all",DtoCuenta[].class);

        //-- Test
        Assertions.assertTrue(response.getBody().length > 0);
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @DisplayName("findAllUsers() - NotDataFound")
    @Order(4)
    void findAllUsers2() throws JsonProcessingException {

        //-- Servicio
        ResponseEntity<String> response= this.testRestTemplate.getForEntity("/cuenta/ver/all", String.class);

        //-- Mapeando Respuesta a JSON
        JsonNode json= objectMapper.readTree(response.getBody().toString());

        //-- Test
        Assertions.assertNotNull(json);
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        //-- Ayuda
        System.out.println(json.path("message"));
        System.out.println(json.path("code"));
        System.out.println(json.path("timestamp"));


    }

    //------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("deleteById() - Exito")
    @Order(5)
    void deleteById() {

        //--Servicio
        this.testRestTemplate.delete("http://localhost:8080/cuenta/delete/1");
    }

    @Test
    @DisplayName("deleteById() - NotFound")
    @Order(5)
    void deleteById2() {

        this.testRestTemplate.delete("http://localhost:8080/cuendsdsdsta/delete/rrrrr");


    }

    //------------------------------------------------------------------------------------------------
    @Test
    void transferirDinero() {
    }
}