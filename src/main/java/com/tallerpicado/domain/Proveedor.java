package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PROVEEDORES")
@Data
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROVEEDOR")
    private Long id;

    @Column(name = "NOMBRE_EMPRESA", nullable = false, length = 100)
    private String nombreEmpresa;

    @Column(name = "CELULAR", length = 20)
    private String celular;

    @Column(name = "CORREO", length = 100)
    private String correo;
}
