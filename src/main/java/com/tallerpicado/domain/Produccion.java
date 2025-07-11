package com.tallerpicado.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "PRODUCCION")
@Data
public class Produccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCCION")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_MAQUINA", nullable = false)
    private Maquinaria maquina;

    @ManyToOne
    @JoinColumn(name = "ID_ORDEN", nullable = false)
    private OrdenTrabajo orden;

    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado; // EN_PROCESO, FINALIZADA, CANCELADA
}
