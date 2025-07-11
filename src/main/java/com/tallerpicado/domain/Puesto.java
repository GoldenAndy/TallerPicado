package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PUESTOS")
@Data
public class Puesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PUESTO")
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;
}
