package com.mokitoyjunit.springmokitoyjunit;

import com.mokitoyjunit.springmokitoyjunit.dtos.DtoResponse;
import com.mokitoyjunit.springmokitoyjunit.dtos.DtoTransferirDinero;
import com.mokitoyjunit.springmokitoyjunit.models.DtoBanco;
import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Datos {


/* ========================== Datos de Prueba: Cuentas ==========================*/
    public static Optional<DtoCuenta> getCuenta01(){
        Optional<DtoCuenta> cuenta01= Optional.of(new DtoCuenta(1L,"persona 1", new BigDecimal("1000")));
        return cuenta01;
    }

    public  static Optional<DtoCuenta> getCuenta02(){
        Optional<DtoCuenta> cuenta02=Optional.of( new DtoCuenta(2L,"persona 2", new BigDecimal("2000")));
        return cuenta02;
    }

    public  static Optional<DtoCuenta> getCuenta03Null(){
        Optional<DtoCuenta> cuenta03= Optional.ofNullable(null);
        return cuenta03;
    }

    public static Optional<DtoBanco> getBanco01(){
        Optional<DtoBanco> banco03=Optional.of(new DtoBanco(1L,"BBVA",0));
        return banco03;
    }

    public static List<DtoCuenta> getAllCuentas(){
        List<DtoCuenta> list=new ArrayList<>();
        list.add(Datos.getCuenta01().get());
        list.add(Datos.getCuenta02().get());
        return list;
    }

    public static List<DtoCuenta> getAllCuentasNull(){
        return null;
    }

    /* =================== Datos de Prueba: DtoTransferirDinero ==========================*/
    public static DtoTransferirDinero getTransferencia1(){return new DtoTransferirDinero(1L,2L,new BigDecimal("100"),1L);};
    public static DtoTransferirDinero getTransferencia2(){return new DtoTransferirDinero(2L,1L,new BigDecimal("100"),1L);};


    /* ========================== Datos de Prueba: DtoResponse ==========================*/
    public static DtoResponse getDtoResponse1(){
        return new DtoResponse("transferencia exitosa", true);
    }

    public static DtoResponse getDtoResponse2(){
        return new DtoResponse("¡Transferencia exitosa!", true);
    }








}
