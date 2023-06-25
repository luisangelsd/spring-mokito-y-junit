package com.mokitoyjunit.springmokitoyjunit.services;

import com.mokitoyjunit.springmokitoyjunit.Datos;
import com.mokitoyjunit.springmokitoyjunit.exceptions.ExceptionSaldoUnsuficiente;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryBanco;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryCuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;



//-- Test: Services
//-- Aquí estamos probando directamente los servicios
//-- Probamos los métodos directamente sin recibir datos de los Repositorios y ni a los Controllers
//-- Aquí la inyección de los repositorios será con Mockito
//-- Implementamos Test (Con datos estáticos)


@ExtendWith(MockitoExtension.class)
class Notas_ServiciosUsuarioTest_ConAnotaciones_Mockito_DatosSimulados {


    //== Añadimos nuestras Interfaces Repositories
    @Mock
    private IRepositoryCuenta repositoryCuenta;

    @Mock
    private IRepositoryBanco repositoryBanco;



    //-- Aquí implementamos inyectamos las interfaces de arriba a nuestro servicio
    //-- Aquí es la implementación no la Interfaz
    //-- Recuerda tener un constructor que pueda recibir las Interfaces de los Repositorios
    @InjectMocks
    private ServiciosUsuario serviciosUsuario;





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