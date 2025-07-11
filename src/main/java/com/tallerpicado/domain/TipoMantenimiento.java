package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TIPO_MANTENIMIENTO")
@Data
public class TipoMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TIPO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;
}
