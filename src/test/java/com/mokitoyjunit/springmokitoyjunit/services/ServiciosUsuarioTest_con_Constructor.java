package com.mokitoyjunit.springmokitoyjunit.services;

import static org.junit.jupiter.api.Assertions.*;
import com.mokitoyjunit.springmokitoyjunit.Datos;
import com.mokitoyjunit.springmokitoyjunit.exceptions.ExceptionSaldoUnsuficiente;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryBanco;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryCuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;


//-- Test: Services
//-- Aquí estamos probando directamente los servicios
//-- Probamos los métodos directamente sin recibir datos de los Repositorios y ni a los Controllers
//-- Aquí la inyección de los repositorios será con Constructor
//-- Implementamos Test (Con datos estáticos)

class Notas_ServiciosUsuarioTest_ConConstructor_DatosSimulados {


    //== Añadimos nuestras Interfaces Repositories
    private IRepositoryCuenta repositoryCuenta;
    private IRepositoryBanco repositoryBanco;

    private IServiciosUsuario serviciosUsuario;



    //== Añadimos la implementación (En este caso una con Datos Estaticos) he inicializamos nuestro servicio con sus Implementaciones
    @BeforeEach
    void beforeEach() {
        this.repositoryCuenta = Mockito.mock(IRepositoryCuenta.class);
        this.repositoryBanco= Mockito.mock(IRepositoryBanco.class);
        this.serviciosUsuario= new ServiciosUsuario( this.repositoryBanco,this.repositoryCuenta);
    }





    @Test
    @DisplayName("transferirDinero() -> Funcionando Correctamente")
    void transferirDinero() {

        //-- Datos: Añadimos la fuente de datos
        Mockito.when(this.repositoryCuenta.findById(1L)).thenReturn(Datos.getCuenta01()); //-- Tiene 1000 pesos
        Mockito.when(this.repositoryCuenta.findById(2L)).thenReturn(Datos.getCuenta02()); //-- Tiene 2,000 pesos
        Mockito.when(this.repositoryBanco.findById(1L)).thenReturn(Datos.getBanco01());  //-- Tiene 3,000 pesos

        //-- Servicios: Enviamos 500 pesos
        serviciosUsuario.transferirDinero(1L, 2L, new BigDecimal("500"), 1L);

        //-- Obtener Valores: Obtenemos los saldos
        BigDecimal cuentaOrigen = this.serviciosUsuario.revisarSaldoCuentaById(1L);
        BigDecimal cuentaDestino= this.serviciosUsuario.revisarSaldoCuentaById(2L);
        Integer totalTransferencias=this.serviciosUsuario.obtenerTotalTransferenciasBancoById(1L);

        //-- Test
        assertEquals("500",cuentaOrigen.toPlainString());     //-- Test: Cuenta Origne
        assertEquals("2500",cuentaDestino.toPlainString());   //-- Test: Cuenta destino
        assertEquals(1,totalTransferencias);                  //-- Test: Total Transferencias


        //-- Verify: Verificamos que se hayan llamado los metodos (Estos se ejecutan 3 veces, pero puede variar)
        Mockito.verify(this.repositoryCuenta, Mockito.times(2)).findById(1L);
        Mockito.verify(this.repositoryCuenta, Mockito.times(2)).findById(2L);
        Mockito.verify(this.repositoryBanco, Mockito.times(2)).findById(1L);

    }


    @Test
    @DisplayName("transferirDinero() -> Simulando Exception")
    void transferirDineroSimulandoException() {

        //-- Datos: Añadimos la fuente de datos
        Mockito.when(this.repositoryCuenta.findById(1L)).thenReturn(Datos.getCuenta01()); //-- Tiene 1000 pesos
        Mockito.when(this.repositoryCuenta.findById(2L)).thenReturn(Datos.getCuenta02()); //-- Tiene 2,000 pesos
        Mockito.when(this.repositoryBanco.findById(1L)).thenReturn(Datos.getBanco01());  //-- Tiene 3,000 pesos

        //-- Servicios: Ejecutamos servicio, Enviamos 500 pesos y creamos el error
        assertThrows(ExceptionSaldoUnsuficiente.class, ()->{
            serviciosUsuario.transferirDinero(1L, 2L, new BigDecimal("1100"), 1L);
        });
    }



}