package com.mokitoyjunit.springmokitoyjunit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokitoyjunit.springmokitoyjunit.Datos;
import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Notas_ControllerCuentaTest_ResTeplate {



    @Autowired
    private TestRestTemplate restTemplate;


    private ObjectMapper mapper=new ObjectMapper();


    private String url="http://localhost:8080";


    @Test
    @Order(1)
    void enviarDineroTest() throws JsonProcessingException {

        //-- Consumimos el servicio, lo mapeamos a un Dto y lo convertimos a un String
        ResponseEntity<String> response= this.restTemplate.postForEntity(url+"/cuenta/transferir", Datos.getTransferencia2(), String.class);

       //-- Obtenemos información del Request

        //-- Obtenemos infomormacion del Body y lo mapeamos a un JSON
        JsonNode jsonBody= mapper.readTree( response.getBody().toString());

        //-- Realizamos nuestros Test
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(jsonBody);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        Assertions.assertEquals("transferencia exitosa",jsonBody.path("mensaje").asText());
//        Assertions.assertEquals(true, jsonBody.path("status").asBoolean());
      //Assertions.assertEquals("otroValor",jsonBody.path("objeto").path("campo").asText());        //-- Ejemplo con datos anidados
      //  Assertions.assertEquals(mapper.writeValueAsString(Datos.getDtoResponse1()),jsonBody);       //-- Response: Comparar por JSON
    }

    @Test
    @Order(2)
    void findById(){
        //-- Consumimos el servicio y lo mapeamos a un Dto
        ResponseEntity<DtoCuenta> dto=this.restTemplate.getForEntity(url+"/cuenta/ver/1", DtoCuenta.class);

        //-- Realizamos pruebas
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1L, dto.getBody().getId());
        Assertions.assertEquals("persona 1", dto.getBody().getPersona());
        Assertions.assertEquals("600.00", dto.getBody().getSaldo().toPlainString());
    }


    @Test
    @Order(3)
    void listAllTest() throws JsonProcessingException {
        //-- Consumimos el servicio y lo mapeamos a un Dto
        ResponseEntity<  DtoCuenta[]> response=this.restTemplate.getForEntity(url+"/cuenta/ver/all", DtoCuenta[].class);

        //-- Convertimos nuestro cuerpo a un List (Opcion 1)
        List<DtoCuenta>list= Arrays.asList(response.getBody());

        //-- Convertimos nuestro cuerpo a un List (Opcion 2)
        JsonNode jsonNode= mapper.readTree(mapper.writeValueAsString(list));

        /* Realizamos pruebas con el list*/
        Assertions.assertNotNull(list);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1L, list.get(0).getId());
        Assertions.assertEquals("persona 1", list.get(0).getPersona());
        Assertions.assertEquals("600.00", list.get(0).getSaldo().toPlainString());

        //-- Realizamos pruebas con el JSON
        Assertions.assertNotNull(jsonNode);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1L,         jsonNode.get(0).path("id").asLong());
        Assertions.assertEquals("persona 1",jsonNode.get(0).path("persona").asText());
        Assertions.assertEquals("600.0",    jsonNode.get(0).path("saldo").asText());
    }

}