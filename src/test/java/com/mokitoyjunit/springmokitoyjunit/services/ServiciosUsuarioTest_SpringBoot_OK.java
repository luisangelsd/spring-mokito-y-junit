package com.mokitoyjunit.springmokitoyjunit.services;

import com.mokitoyjunit.springmokitoyjunit.Datos;
import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryBanco;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryCuenta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;


//-- Implementación: SpringBoot
@SpringBootTest
class ServiciosUsuarioTest_SpringBoot_OK {

    @MockBean
    private IRepositoryCuenta iRepositoryCuenta;

    @MockBean
    private IRepositoryBanco iRepositoryBanco;

    @Autowired
    @Qualifier("serviciosUsuario")
    private IServiciosUsuario serviciosUsuarios;

    private DtoCuenta dtoCuenta;
    private List<DtoCuenta> listDtoCuentas;

    @BeforeEach
    void beforeEach(){
        this.dtoCuenta=new DtoCuenta();
        this.listDtoCuentas=new ArrayList<>();
    }


    // =================================================================== //
    @Test
    @DisplayName("findById() - Dato Encontrado - Regresa un DTO")
    void findById() {

        //-- Datos
        //Mockito.when(this.iRepositoryCuenta.findById(1L)).thenReturn(Datos.getCuenta01());

        //-- Servicio
        dtoCuenta = this.serviciosUsuarios.findById(1L);

        //-- Pruebas
        Assertions.assertNotNull(dtoCuenta);
        Assertions.assertEquals("persona 1", dtoCuenta.getPersona());
        Assertions.assertEquals("1000", dtoCuenta.getSaldo().toPlainString());
    }



    @Test
    @DisplayName("findById() - Dato no Encontrado - Regresa null")
    void findByIdNoFound() {

        //-- Datos
       Mockito.when(this.iRepositoryCuenta.findById(3L)).thenReturn(Datos.getCuenta03Null());

        //-- Consumimos el servicio
        this.dtoCuenta = this.serviciosUsuarios.findById(3L);

        Assertions.assertNull(this.dtoCuenta);

    }


//    @Test
//    @DisplayName("findById() - Ocurrio una Exception")
//    void findByIdThrowException() {
//
//
//        //-- Aqui generamos la exception al proposito -- (AQUI LO IMPORTANTE)
//        Mockito.when(this.iRepositoryCuenta.findById(1L)).thenThrow(Exception.class);
//
//
//        //-- Consumimos el servicio
//        Assertions.assertThrows( Exception.class , ()->{
//            this.dtoCuenta = this.serviciosUsuarios.findById(1L);
//        });
//
//        //Assertions.assertEquals(Exception, exception);
//
//    }




    // =================================================================== //



    @Test
    @DisplayName(value="findAll - Datos Encontrados - Return List<Dto>")
    void findAll() {

        //-- Datos
        Mockito.when(this.iRepositoryCuenta.findAll()).thenReturn(Datos.getAllCuentas());

        //-- Servicio
        this.listDtoCuentas=this.serviciosUsuarios.findAll();

        //-- Test: No debe ser null
        Assertions.assertNotNull(this.listDtoCuentas);

    }

    @Test
    @DisplayName(value="findAll - Datos No Encontrados - Return null")
    void findAllNotFound() {

        //-- Datos
        Mockito.when(this.iRepositoryCuenta.findAll()).thenReturn(Datos.getAllCuentasNull());

        //-- Servicio
        this.listDtoCuentas = this.serviciosUsuarios.findAll();


        //-- Test: No debe ser null
        Assertions.assertNull(this.listDtoCuentas);

    }

    // =================================================================== //


    @Test
    @DisplayName(value = "saveUpdate - Actualizanod OK")
    void saveUpdate() {

        DtoCuenta dto=Datos.getCuenta01().get();
        dto.setPersona("persona 1 - editado");

        //-- Simulamos Datos



    }




    // =================================================================== //
    @Test
    void deleteById() {
    }



    // =================================================================== //
    @Test
    void obtenerTotalTransferenciasBancoById() {
    }

    // =================================================================== //
    @Test
    void revisarSaldoCuentaById() {
    }

    // =================================================================== //
    @Test
    void transferirDinero() {
    }
}