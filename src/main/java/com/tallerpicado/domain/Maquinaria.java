package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "MAQUINARIA")
@Data
public class Maquinaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MAQUINA")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "MARCA", length = 50)
    private String marca;

    @Column(name = "FECHA_ADQUISICION")
    @Temporal(TemporalType.DATE)
    private Date fechaAdquisicion;

    @Column(name = "ESTADO", length = 20)
    private String estado;
}
