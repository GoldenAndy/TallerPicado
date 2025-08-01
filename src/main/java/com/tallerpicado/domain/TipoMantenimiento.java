package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TIPO_MANTENIMIENTO")
@Data
public class TipoMantenimiento {

    @Id
    @Column(name = "ID_TIPO")
    private Long id; // Se debe establecer manualmente ANTES de guardar

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;
}
