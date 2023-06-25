package com.mokitoyjunit.springmokitoyjunit.controllers;

import com.mokitoyjunit.springmokitoyjunit.dtos.DtoResponse;
import com.mokitoyjunit.springmokitoyjunit.dtos.DtoTransferirDinero;
import com.mokitoyjunit.springmokitoyjunit.exceptions.ExceptionSaldoUnsuficiente;
import com.mokitoyjunit.springmokitoyjunit.exceptions.RuntimeExceptionDataNotFound;
import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import com.mokitoyjunit.springmokitoyjunit.services.IServiciosUsuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cuenta/")
public class ControllerCuenta {

    //-- Variables globales

    List<DtoCuenta> listDtoCuenta;
    DtoCuenta dtoCuenta;


    //-- Implementaciones
    @Autowired(required = true)
    @Qualifier("serviciosUsuario")
    IServiciosUsuario serviciosUsuario;



    // --------------------------- CRUD CUENTA --------------------------- //


    @PostMapping("save")
    @ResponseStatus(HttpStatus.CREATED)
    public DtoCuenta saveCuenta(@Valid @RequestBody DtoCuenta dtoRequest){

            //-- Service
            dtoRequest.setId(null);
            this.dtoCuenta = this.serviciosUsuario.saveUpdate(dtoRequest);

            //-- Response
            return this.dtoCuenta;
    }


    @PutMapping("update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DtoCuenta updateCuenta( @PathVariable(name = "id", required = true) Long id, @Valid @RequestBody DtoCuenta dtoCuenta){


            //-- Validar si Existe
            this.dtoCuenta=this.serviciosUsuario.findById(id);
            if (this.dtoCuenta==null){
                throw new RuntimeExceptionDataNotFound ("Lo sentimos, la cuenta a editar no Existe");
            }

            //-- Solicitar Servicio
            dtoCuenta.setId(id);
            this.dtoCuenta = this.serviciosUsuario.saveUpdate(dtoCuenta);


            //-- Enviar Respuesta
            return this.dtoCuenta;
    }


    //-- Buscar por Id
    @GetMapping("ver/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DtoCuenta verUsuarioById(@PathVariable(name = "id") Long id) {

            //-- Consumir servicio
            this.dtoCuenta = this.serviciosUsuario.findById(id);

            //-- Validar si no esta vacio
            if (this.dtoCuenta==null) {
                throw new RuntimeExceptionDataNotFound("La cuenta no existe");
            }

            //-- Regresar respuesta
            return this.dtoCuenta;
    }


    //-- Listar todos
    @GetMapping("ver/all")
    @ResponseStatus(HttpStatus.OK)
    public List<DtoCuenta> findAllUsers() {


            //-- Validar que existan registros
            this.listDtoCuenta = this.serviciosUsuario.findAll();
            if (this.listDtoCuenta==null) {
               throw new RuntimeExceptionDataNotFound("Lo sentimos, no hay registros a mostrar");
            }

            //-- Regresar respuesta
            return this.listDtoCuenta;

    }


    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DtoCuenta deleteById(@PathVariable(required = true) Long id){

        //-- validar que exista
        this.dtoCuenta=this.serviciosUsuario.findById(id);

        if (this.dtoCuenta==null){
            throw new RuntimeExceptionDataNotFound("La cuenta a Eliminar no Existe");
        }

        //-- Eliminar
        this.serviciosUsuario.deleteById(id);
        return this.dtoCuenta;

    }


// --------------------------- OTROS --------------------------- //

    @PostMapping(path = "/transferir")
    @ResponseStatus(HttpStatus.GONE)
    public DtoResponse transferirDinero(@Valid @RequestBody DtoTransferirDinero dto){


            //-- Validar que exista cuenta origen
            DtoCuenta cOrigen=this.serviciosUsuario.findById(dto.getIdCuentaOrigen());
            if (cOrigen==null){
                throw new RuntimeExceptionDataNotFound("No existe la cuenta origen");
            }

            //-- Validar que exista destino
        DtoCuenta cDestino= this.serviciosUsuario.findById(dto.getIdCuentaDestino());
            if (cDestino == null){
                throw new RuntimeExceptionDataNotFound("No existe la cuenta Destino");
            }

            //-- Validar Dinero
        BigDecimal saldoEstimado=cOrigen.getSaldo().subtract(dto.getMonto());
        if (saldoEstimado.compareTo(BigDecimal.ZERO)<0.){
            throw new ExceptionSaldoUnsuficiente("La cuenta "+dto.getIdCuentaOrigen()+" no tiene saldo suficiente");
        }

            //-- Realizar el servicio
            this.serviciosUsuario.transferirDinero(dto.getIdCuentaOrigen(), dto.getIdCuentaDestino(), dto.getMonto(),dto.getIdBanco());


            //-- Enviar respuesta
            return  new  DtoResponse("transferencia exitosa", true);
    }


}


