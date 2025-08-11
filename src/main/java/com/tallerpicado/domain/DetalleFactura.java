package com.tallerpicado.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DETALLE_FACTURA")
@Data
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DF_SEQ")
    @SequenceGenerator(name = "DF_SEQ", sequenceName = "DETALLE_FACTURA_SEQ", allocationSize = 1)
    @Column(name = "ID_DETALLE")
    private Long id;

    @Column(name = "ID_FACTURA", nullable = false)
    private Long idFactura;

    @Column(name = "ID_ARTICULO", nullable = false)
    private Long idArticulo;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private Double precioUnitario;

    @Column(name = "SUBTOTAL", nullable = false)
    private Double subtotal;

    @Transient
    private String nombreArticulo;

    public DetalleFactura() {}
}
