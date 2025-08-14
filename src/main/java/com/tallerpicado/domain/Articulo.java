package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ARTICULOS", schema = "sys") 
@Data
@NoArgsConstructor

public class Articulo {

    @Id
    @Column(name = "ID_ARTICULO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "PRECIO", nullable = false)
    private Double precio;

    @Column(name = "STOCK")
    private Integer stock;

    @Column(name = "TIPO", nullable = false, length = 1)
    private String tipo; 

    public Articulo(Long id) {
        this.id = id;
    }
}
