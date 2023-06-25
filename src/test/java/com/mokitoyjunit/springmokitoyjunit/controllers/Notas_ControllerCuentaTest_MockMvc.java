package com.mokitoyjunit.springmokitoyjunit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokitoyjunit.springmokitoyjunit.Datos;
import com.mokitoyjunit.springmokitoyjunit.dtos.DtoTransferirDinero;
import com.mokitoyjunit.springmokitoyjunit.services.IServiciosUsuario;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



//-- Test: Controllers
//-- Probamos los endpoints directamente sin pasar por el Services
//-- Aquí simularemos nuestro service con datos de prueba y solo validaremos el controller con su response y request :)

@SpringBootTest
@AutoConfigureMockMvc
class Notas_ControllerCuentaTest_MockMvc {


    //-- Inyectamos servicio
    @Autowired
    private MockMvc mockMvc;


    //-- Inyectamos nuestra Interfaz
    @MockBean
    private IServiciosUsuario iServiciosUsuario;







    @Test//-- Ejemplo: Get
    void verUsuarioByIdTest() throws Exception {

        //-- Datos alternativos al servicio
        Mockito.when(this.iServiciosUsuario.findById(1L)).thenReturn(Datos.getCuenta01().get());

        //-- Aquí las pruebas de nuestro Controller
        mockMvc.perform(MockMvcRequestBuilders.get("/cuenta/ver/1")             //-- Endpoind: url
                .contentType(MediaType.APPLICATION_JSON))                                 //-- Request: Formato en el cual enviaremos información - Request (aquí no es necesario porque no envia nada paps)
                .andExpect(MockMvcResultMatchers.status().isOk())                         //-- Response: Status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))             //-- Response: Tipo de datos que se regresaran en el Body
                .andExpect(jsonPath("$.persona").value("persona 1"))  //--Response: Obtener parametros del JSON y comparar su contenido
                .andExpect(jsonPath("$.saldo").value(100))           //--Response: Obtener parametros del JSON y comparar su contenido
                .andExpect(jsonPath("$.otroParametro").doesNotExist());         //-- Que no exista este parametro
    }



    @Test //-- Ejemplo: Post
    void transferirDineroTest() throws Exception {

        //-- Nos sirve para mapear un objeto y convertir a JSON
        ObjectMapper m=new ObjectMapper();

        //-- Simulamos los datos de nuestro service
        DtoTransferirDinero dto=new DtoTransferirDinero(1L,2L,new BigDecimal("1000"),1L);

        //-- Aquí las pruebas de nuestro Controller
        mockMvc.perform(MockMvcRequestBuilders.post("/cuenta/transferir")                        //-- Endpoind: url
                .contentType(MediaType.APPLICATION_JSON).content(m.writeValueAsString(dto)))               //-- Request: Formato en el cual recibiremos información - Request
                .andExpect(MockMvcResultMatchers.status().isOk())                                         //-- Response: Status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))                             //-- Response: Tipo de datos que se regresaran en el Body
                .andExpect(jsonPath("$.mensaje").value("transferencia exitosa"))    //--Response: Obtener parametros del JSON y comparar su contenido
                .andExpect(jsonPath("$.status", Matchers.is(true)));           //-- Response: Obtener parametros del JSON y comparar su contenido (de una forma diferente)
    }



    @Test //-- Ejemplo Post: Este es el mismo metodo anterior, la diferencia es que aquí compararemos los datos del response por JSON completo y no por parametro en espesifico
    void transferirDineroTest2() throws Exception {

        //-- Nos sirve para mapear un objeto y convertir a JSON
        ObjectMapper m=new ObjectMapper();

        //-- Simulamos los datos de nuestro service
        DtoTransferirDinero dto=new DtoTransferirDinero(1L,2L,new BigDecimal("1000"),1L); //-- Simulamos los datos del body, de la peticion http

        //-- Aquí las pruebas de nuestro Controller
        mockMvc.perform(MockMvcRequestBuilders.post("/cuenta/transferir")                   //-- Endpoind: url
                .contentType(MediaType.APPLICATION_JSON).content(m.writeValueAsString(dto)))         //-- Request: Formato en el cual recibiremos información - Request
                .andExpect(MockMvcResultMatchers.status().isOk())                                    //-- Response: Status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))                        //-- Response: Tipo de datos que se regresaran en el Body
                .andExpect(content().json(m.writeValueAsString(Datos.getDtoResponse1())));          //-- Response Validamos compos de del cuerpo del body por JSON completo
    }



    @Test
    void findAllUsersTest() throws Exception {
        //-- Simulamos los datos de nuestro service
        Mockito.when(this.iServiciosUsuario.findAll()).thenReturn(Datos.getAllCuentas());

        //-- Aquí las pruebas de nuestro Controller
        mockMvc.perform(MockMvcRequestBuilders.get("/cuenta/ver/all")                 //-- Url de nuestro Endpoind
                .contentType(MediaType.APPLICATION_JSON))                                      //-- Request: Formato en el cual recibiremos información - Request (aquí no es necesario porque no envia nada paps)
                .andExpect(MockMvcResultMatchers.status().isOk())                             //-- Response: Status que debe regresar
                .andExpect(jsonPath("$.[0].persona").value("persona 1")) //-- Response: Comparar datos del Json de la respuesta
                .andExpect(jsonPath("$.[0].id", Matchers.is(1)));             //-- Response: Comparar datos del Json de la respuesta (de una forma diferente)
    }
}