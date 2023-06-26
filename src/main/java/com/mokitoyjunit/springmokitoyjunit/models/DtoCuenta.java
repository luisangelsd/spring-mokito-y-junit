package com.mokitoyjunit.springmokitoyjunit.models;

import com.mokitoyjunit.springmokitoyjunit.exceptions.RuntimeExceptionSaldoUnsuficiente;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter @Setter  @EqualsAndHashCode
@Table(name = "cuentas")
public class DtoCuenta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name="persona")
    private String persona;

    @NotNull
    @Column(name = "saldo")
    private BigDecimal saldo;


    //============ Constructores ============
    public DtoCuenta(){}
    public DtoCuenta(Long id, String persona, BigDecimal saldo) {
        this.id = id;
        this.persona = persona;
        this.saldo = saldo;
    }

    public DtoCuenta( String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    // ============ Metodos ============
    public void agregarDinero(BigDecimal monto){
        this.saldo=this.saldo.add(monto);
    }

    public void quitarDinero(BigDecimal monto){
        BigDecimal nuevoSaldo=this.saldo.subtract(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO)<0){
            throw  new RuntimeExceptionSaldoUnsuficiente("Saldo Insuficiente: Actualmente tienes: $"+this.saldo+" y quieres restar: $"+monto);
        }
        this.saldo=this.saldo.subtract(monto);
    }


    // ============ Getters and Setters: Lombook ============
    // ============ Equals y hashCode: Lombook ============


    @Override
    public String toString() {
        return "DtoCuenta{" +
                "id=" + id +
                ", persona='" + persona + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
