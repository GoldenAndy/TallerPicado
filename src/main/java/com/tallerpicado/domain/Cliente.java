package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CLIENTES")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "CEDULA", length = 20)
    private String cedula;

    @Column(name = "CELULAR", length = 20)
    private String celular;

    @Column(name = "CORREO", length = 100)
    private String correo;
}
