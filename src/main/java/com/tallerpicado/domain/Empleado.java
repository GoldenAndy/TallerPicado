package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "EMPLEADOS")
@Data
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    @Column(name = "ID_PUESTO")
    private Long idPuesto;

    @Column(name = "CELULAR", length = 20)
    private String celular;

    @Column(name = "CORREO", length = 100)
    private String correo;

    @Column(name = "ESTADO", length = 20)
    private String estado; // Considera usar Enum si quieres más control
}
