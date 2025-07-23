package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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
    private String estado;

    
    @Transient
    private String nombrePuesto;

    
    @Transient
    private List<String> proveedoresAsignados;

    
    @Transient
    private List<Long> proveedores;


    public List<Long> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Long> proveedores) {
        this.proveedores = proveedores;
    }
    
    // Constructor vacío requerido por JPA
    public Empleado() {}
}
