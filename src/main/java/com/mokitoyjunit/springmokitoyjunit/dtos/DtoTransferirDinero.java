package com.mokitoyjunit.springmokitoyjunit.dtos;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter @Setter
public class DtoTransferirDinero {

    @NotNull
    private Long idCuentaOrigen;
    @NotNull
    private Long idCuentaDestino;
    @NotNull
    private BigDecimal monto;
    @NotNull
    private Long idBanco;


    public DtoTransferirDinero(Long idCuentaOrigen, Long idCuentaDestino, BigDecimal monto, Long idBanco) {
        this.idCuentaOrigen = idCuentaOrigen;
        this.idCuentaDestino = idCuentaDestino;
        this.monto = monto;
        this.idBanco = idBanco;
    }

    public DtoTransferirDinero(){

    }






}
