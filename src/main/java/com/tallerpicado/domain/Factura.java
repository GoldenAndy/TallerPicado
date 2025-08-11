package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "FACTURAS")
@Data
@NoArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FACTURAS_SEQ_GEN")
    @SequenceGenerator(name = "FACTURAS_SEQ_GEN", sequenceName = "FACTURAS_SEQ", allocationSize = 1)
    @Column(name = "ID_FACTURA")
    private Long id;

    @Column(name = "FECHA_EMISION", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaEmision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    private Empleado empleado;

    @Column(name = "TOTAL", nullable = false)
    private Double total = 0d;  // el trigger + package lo recalculan, aquí solo aseguramos no-nulo

    public Factura(Long id) {
        this.id = id;
    }
}
