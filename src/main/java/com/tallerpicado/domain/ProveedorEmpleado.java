package com.tallerpicado.domain;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PROVEEDORES_EMPLEADOS")
@Data
@IdClass(ProveedorEmpleadoId.class)
public class ProveedorEmpleado {

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_PROVEEDOR", nullable = false)
    private Proveedor proveedor;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    private Empleado empleado;
}