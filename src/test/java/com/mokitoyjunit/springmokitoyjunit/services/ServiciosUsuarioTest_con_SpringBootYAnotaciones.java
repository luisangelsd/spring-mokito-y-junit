package com.mokitoyjunit.springmokitoyjunit.services;

import com.mokitoyjunit.springmokitoyjunit.Datos;
import com.mokitoyjunit.springmokitoyjunit.exceptions.RuntimeExceptionSaldoUnsuficiente;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryBanco;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryCuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;



//-- Test: Services
//-- Aquí estamos probando directamente los servicios
//-- Probamos los métodos directamente sin recibir datos de los Repositorios y ni a los Controllers
//-- Aquí la inyección de los repositorios será con SpringBoot
//-- Implementamos Test (Con datos estáticos)


@SpringBootTest
class ServiciosUsuarioTest_con_SpringBootYAnotaciones {


    //== Añadimos nuestras Interfaces Repositories
    //-- Estas se inyectan automaticamente a nuestra Interfaz de services
    @MockBean
    private IRepositoryCuenta repositoryCuenta;
    @MockBean
    private IRepositoryBanco repositoryBanco;



    //-- Aquí implementamos inyectamos las interfaces de arriba a nuestro servicio
    //-- Aquí es la implementacion, pero registra este componente con cualquier bean como @service
    //-- Coloca el nombre del servicio de la implementación en caso de que tengas varios
    @Autowired
    @Qualifier("serviciosUsuario")
    private IServiciosUsuario serviciosUsuario;






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
        assertThrows(RuntimeExceptionSaldoUnsuficiente.class, ()->{
            serviciosUsuario.transferirDinero(1L, 2L, new BigDecimal("1100"), 1L);
        });
    }


}