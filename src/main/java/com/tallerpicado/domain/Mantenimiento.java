package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "MANTENIMIENTO")
@Data
public class Mantenimiento {

    @Id
    @Column(name = "ID_MANTENIMIENTO")
    private Long id;

    @Column(name = "ID_MAQUINA", nullable = false)
    private Long idMaquina;

    @Column(name = "ID_TIPO", nullable = false)
    private Long idTipo;

    @Column(name = "FECHA_MANTENIMIENTO", nullable = false)
    private LocalDate fecha;

    @Lob
    @Column(name = "DESCRIPCION")
    private String descripcion;
}
