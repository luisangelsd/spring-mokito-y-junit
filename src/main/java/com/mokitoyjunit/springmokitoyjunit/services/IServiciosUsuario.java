package com.mokitoyjunit.springmokitoyjunit.services;

import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;



public interface IServiciosUsuario {

    // ------------ Cuentas ------------//

    public DtoCuenta saveUpdate(DtoCuenta dtoCuenta);

    public void deleteById(Long id);

    public DtoCuenta findById(Long id);

    public List<DtoCuenta> findAll();



    // ------------ Otros ------------//
    public Integer obtenerTotalTransferenciasBancoById(Long id);

    public BigDecimal revisarSaldoCuentaById(Long id);

    public void transferirDinero(Long idCuentaOrigen, Long idCuentaDestino,  BigDecimal monto, Long bancoId);
}
