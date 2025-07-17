package com.tallerpicado.domain;


import java.time.LocalDate;

import jakarta.persistence.*;


@Entity
@Table(name = "MAQUINARIA") // cambia TALLERPICADO por tu esquema
public class Maquinaria {
    @Id
@Column(name = "ID_MAQUINA") 
private Long id;
  

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "MARCA")
    private String marca;

    @Column(name = "FECHA_ADQUISICION")
    private LocalDate fechaAdquisicion;

    @Column(name = "ESTADO")
    private String estado;
    // Constructor vacío obligatorio para JPA
    public Maquinaria() {
    }

    // Constructor con todos los campos
    public Maquinaria(String estado, LocalDate fechaAdquisicion, Long id, String marca, String nombre) {
        this.estado = estado;
        this.fechaAdquisicion = fechaAdquisicion;
        this.id = id;
        this.marca = marca;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
//comentario de Charlie
