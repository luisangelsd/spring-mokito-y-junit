package com.mokitoyjunit.springmokitoyjunit.services;

import com.mokitoyjunit.springmokitoyjunit.models.DtoBanco;
import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryBanco;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryCuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service("serviciosUsuario")
public class ServiciosUsuario implements IServiciosUsuario{


    //===== Inyección de Repositories (Dependencias =======
    @Autowired
    private IRepositoryBanco repositoryBanco;

    @Autowired
    private IRepositoryCuenta repositoryCuenta;


    //===== Implementar Repostiories por Constructor =======
    public ServiciosUsuario(IRepositoryBanco repositoryBanco, IRepositoryCuenta repositoryCuenta) {
        this.repositoryBanco = repositoryBanco;
        this.repositoryCuenta = repositoryCuenta;
    }




    //----------------------------------CRUD CUENTAS---------------------------------------

    @Override //-- Save / Update
    public DtoCuenta saveUpdate(DtoCuenta dtoCuenta) {
        return this.repositoryCuenta.save(dtoCuenta);
    }

    @Override//-- Delete
    public void deleteById(Long id) {
        this.repositoryCuenta.deleteById(id);
    }


    @Override //-- Find by id
    public DtoCuenta findById(Long id) {

        DtoCuenta cuenta=this.repositoryCuenta.findById(id).orElse(null);
        return  cuenta;
    }


    @Override //-- FindAll
    public List<DtoCuenta> findAll() {
        return this.repositoryCuenta.findAll();
    }


    //----------------------------------OTROS---------------------------------------
    @Override
    public Integer obtenerTotalTransferenciasBancoById(Long id) {
        DtoBanco banco= this.repositoryBanco.findById(id).orElseThrow();
        Integer totalTransferencias=banco.getTotalTransferencias();
        return totalTransferencias;
    }

    @Override
    public BigDecimal revisarSaldoCuentaById(Long id) {
        DtoCuenta cuenta=this.repositoryCuenta.findById(id).orElseThrow();
        BigDecimal saldo=cuenta.getSaldo();
        return saldo;
    }

    @Override
    public void transferirDinero(Long idCuentaOrigen, Long idCuentaDestino,   BigDecimal monto, Long bancoId) {


        //-- Retirar dinero
        DtoCuenta cuentaOrigen=this.repositoryCuenta.findById(idCuentaOrigen).orElse(null);
        cuentaOrigen.quitarDinero(monto);
        this.repositoryCuenta.save(cuentaOrigen);


        //-- Añadir dinero
        DtoCuenta cuentaDestino=this.repositoryCuenta.findById(idCuentaDestino).orElse(null);
        cuentaDestino.agregarDinero(monto);
        this.repositoryCuenta.save(cuentaDestino);



        //-- Aumentar las transferencias del banco
        DtoBanco banco =this.repositoryBanco.findById(bancoId).orElse(null);
        Integer totalTransferencias= banco.getTotalTransferencias();
        banco.setTotalTransferencias((++totalTransferencias));



    }
}
