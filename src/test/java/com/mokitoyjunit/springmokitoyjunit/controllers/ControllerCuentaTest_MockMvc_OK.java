package com.mokitoyjunit.springmokitoyjunit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokitoyjunit.springmokitoyjunit.Datos;
import com.mokitoyjunit.springmokitoyjunit.dtos.DtoTransferirDinero;
import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import com.mokitoyjunit.springmokitoyjunit.services.IServiciosUsuario;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


//-- Test: Controllers
//-- Probamos los endpoints directamente sin pasar por el Services
//-- Aquí simularemos nuestro service con datos de prueba y solo validaremos el controller con su response y request :)

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControllerCuentaTest_MockMvc_OK {

    //-- Inyectamos servicio
    @Autowired
    private MockMvc mockMvc;


    //-- Inyectamos Interfaz
    @MockBean
    private IServiciosUsuario iServiciosUsuario;




    //-- Para mapear a JSON/DTO
    ObjectMapper objectMapper=new ObjectMapper();


    //---------------------------------------------------------------------------------------
    @Test
    @DisplayName("verUsuarioById() - ¡Con Exito!")
    @Order(1)
    void verUsuarioById() throws Exception {

        //-- Datos
        Mockito.when(this.iServiciosUsuario.findById(1L)).thenReturn(Datos.getCuenta01().get());

        //-- Servicio + Test
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cuenta/ver/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.persona").value("persona 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value(1000));
    }


    @Test
    @DisplayName("verUsuarioById() - ¡NotFound!")
    @Order(1)
    void verUsuarioById2() throws Exception {

        //-- Datos
        Mockito.when(this.iServiciosUsuario.findById(100L)).thenReturn(null);

        //-- Servicio + Test
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cuenta/ver/100")
                 .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }




    //---------------------------------------------------------------------------------------
    @Test
    @DisplayName("findAllUsers() - Encuentra Registros")
    @Order(2)
    void findAllUsers() throws Exception {

        //-- Datos
        Mockito.when(this.iServiciosUsuario.findAll()).thenReturn(Datos.getAllCuentas());

        //-- Servicio + Pruebas
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cuenta/ver/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].persona").value("persona 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].saldo").value(1000));

    }

    @Test
    @DisplayName("findAllUsers() - No Encuentra Registros")
    @Order(2)
    void findAllUsers2() throws Exception {

        //-- Datos
        Mockito.when(this.iServiciosUsuario.findAll()).thenReturn(Datos.getAllCuentasNull());

        //-- Servicio + Pruebas
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cuenta/ver/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }


    //---------------------------------------------------------------------------------------
    @Test
    @DisplayName("saveCuenta() - Guarda OK")
    @Order(3)
    void saveCuenta() throws Exception {


        //-- Datos
        DtoCuenta dtoCuenta=new DtoCuenta(  "persona 3",new BigDecimal("3000"));
       Mockito.when(this.iServiciosUsuario.saveUpdate( dtoCuenta )).thenReturn( dtoCuenta );

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cuenta/save")
                .contentType(MediaType.APPLICATION_JSON).content( objectMapper.writeValueAsString(dtoCuenta)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.persona").value("persona 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value("3000"));
    }


    @Test
    @DisplayName("saveCuenta() - No Guarda - BadRequest")
    @Order(3)
    void saveCuenta2() throws Exception {

        //-- Datos
        DtoCuenta dtoCuenta=new DtoCuenta(  null, new BigDecimal("3000"));
        Mockito.when(this.iServiciosUsuario.saveUpdate( dtoCuenta )).thenReturn( dtoCuenta );

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cuenta/save")
                 .contentType(MediaType.APPLICATION_JSON).content( objectMapper.writeValueAsString(dtoCuenta)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //---------------------------------------------------------------------------------------

    @Test
    @Order(4)
    @DisplayName("updateCuenta() - ¡Actualizado! ")
    void updateCuenta() throws Exception {

        //-- Datos
        DtoCuenta dtoActualizar =new DtoCuenta(1L, "Persona 1 - Actualizada", new BigDecimal("1000"));
        Mockito.when(this.iServiciosUsuario.findById( 1L )).thenReturn( dtoActualizar );
        Mockito.when(this.iServiciosUsuario.saveUpdate( dtoActualizar )).thenReturn( dtoActualizar );

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.put("/cuenta/update/1")
                .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(dtoActualizar)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.persona").value("Persona 1 - Actualizada"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value("1000"));
    }

    @Test
    @Order(4)
    @DisplayName("updateCuenta() - No Actualizado - Cuenta No Encontrada ")
    void updateCuenta2() throws Exception {

        //-- Datos
        DtoCuenta dtoActualizar =new DtoCuenta(1L, "Persona 1 - Actualizada", new BigDecimal("1000"));
        Mockito.when(this.iServiciosUsuario.findById( 1L )).thenReturn( null );

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.put("/cuenta/update/1")
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(dtoActualizar)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    @Order(4)
    @DisplayName("updateCuenta() - No Actualizado - BadRequest ")
    void updateCuenta3() throws Exception {

        //-- Datos
        DtoCuenta dtoActualizar =new DtoCuenta( null, null, new BigDecimal("1000"));

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.put("/cuenta/update/1")
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(dtoActualizar)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    //---------------------------------------------------------------------------------------

    @Test
    @DisplayName("deleteById() - ¡Elimando con Exito!")
    @Order(5)
    void deleteById() throws Exception {

        //-- Datos
        Mockito.when(this.iServiciosUsuario.findById(1L)).thenReturn(Datos.getCuenta01().get());

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cuenta/delete/1")
                .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(Datos.getCuenta01())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.persona").value("persona 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value("1000"));
    }

    @Test
    @DisplayName("deleteById() - No Eleminando - NotFound")
    @Order(5)
    void deleteById2() throws Exception {

        //-- Datos
        Mockito.when(this.iServiciosUsuario.findById(1L)).thenReturn(null);

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cuenta/delete/1")
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(Datos.getCuenta01())))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }



    //---------------------------------------------------------------------------------------
    @Test
    @DisplayName("transferirDinero() - ¡Transferencia Exitosa!")
    @Order(6)
    void transferirDinero() throws Exception {

        //-- Datos
        DtoTransferirDinero dtoTransfe=new DtoTransferirDinero(1L, 2L, new BigDecimal("100"), 1L);
        Mockito.when(this.iServiciosUsuario.findById(1L)).thenReturn(Datos.getCuenta01().get());
        Mockito.when(this.iServiciosUsuario.findById(2L)).thenReturn(Datos.getCuenta02().get());

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cuenta/transferir")
                .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString( dtoTransfe )))
                .andExpect(MockMvcResultMatchers.status().isGone())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("transferirDinero() - Error: CuentaOrigen no Existe")
    @Order(6)
    void transferirDinero2() throws Exception {

        //-- Datos
        DtoTransferirDinero dtoTransfe=new DtoTransferirDinero(1L, 2L, new BigDecimal("100"), 1L);
        Mockito.when(this.iServiciosUsuario.findById(1L)).thenReturn(null);
        Mockito.when(this.iServiciosUsuario.findById(2L)).thenReturn(Datos.getCuenta02().get());

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cuenta/transferir")
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString( dtoTransfe )))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("transferirDinero() - Error: CuentaDestino no Existe")
    @Order(6)
    void transferirDinero3() throws Exception {

        //-- Datos
        DtoTransferirDinero dtoTransfe=new DtoTransferirDinero(1L, 2L, new BigDecimal("100"), 1L);
        Mockito.when(this.iServiciosUsuario.findById(1L)).thenReturn(Datos.getCuenta01().get());
        Mockito.when(this.iServiciosUsuario.findById(2L)).thenReturn(null);

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cuenta/transferir")
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString( dtoTransfe )))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("transferirDinero() - Error: Dinero Insuficiente")
    @Order(6)
    void transferirDinero4() throws Exception {

        //-- Datos
        DtoTransferirDinero dtoTransfe=new DtoTransferirDinero(1L, 2L, new BigDecimal("2000"), 1L);
        Mockito.when(this.iServiciosUsuario.findById(1L)).thenReturn(Datos.getCuenta01().get());
        Mockito.when(this.iServiciosUsuario.findById(2L)).thenReturn(Datos.getCuenta02().get());

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cuenta/transferir")
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString( dtoTransfe )))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("transferirDinero() - Error: BadRequest")
    @Order(6)
    void transferirDinero5() throws Exception {

        //-- Datos
        DtoTransferirDinero dtoTransfe=new DtoTransferirDinero(1L, null, new BigDecimal("2000"), 1L);
        Mockito.when(this.iServiciosUsuario.findById(1L)).thenReturn(Datos.getCuenta01().get());
        Mockito.when(this.iServiciosUsuario.findById(2L)).thenReturn(Datos.getCuenta02().get());

        //-- Enviar Request + Test al Response
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cuenta/transferir")
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString( dtoTransfe )))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

}