package com.mokitoyjunit.springmokitoyjunit.models;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "bancos")
@Getter @Setter
public class DtoBanco implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "total_transferencias")
    private Integer totalTransferencias;


    //============ Constructores: Lombook ============

    public DtoBanco(){}

    public DtoBanco(Long id, String nombre, Integer totalTransferencias) {
        this.id = id;
        this.nombre = nombre;
        this.totalTransferencias = totalTransferencias;
    }

    //============ Metodos ============
    public Integer getTotalTransferencias() {
        return totalTransferencias;
    }

    public void setTotalTransferencias(Integer totalTransferencias) {
        this.totalTransferencias = totalTransferencias;
    }



    // ============ Getters and Setters: Lombook ============
    // ============ Equals y hashCode: Lombook ============
}
