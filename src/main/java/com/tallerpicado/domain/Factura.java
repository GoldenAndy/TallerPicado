package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FACTURAS")
@Data
@NoArgsConstructor

public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FACTURA")
    private Long id;

    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaEmision;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    private Empleado empleado;

    @Column(name = "TOTAL")
    private Double total;

    public Factura(Long id) {
        this.id = id;
    }
}
