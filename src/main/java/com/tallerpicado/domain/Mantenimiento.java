package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "MANTENIMIENTO")
@Data
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MANTENIMIENTO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_MAQUINA", nullable = false)
    private Maquinaria maquina;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO", nullable = false)
    private TipoMantenimiento tipo;

    @Column(name = "FECHA_MANTENIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaMantenimiento;

    @Column(name = "DESCRIPCION")
    private String descripcion;
}
