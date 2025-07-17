package com.tallerpicado.service;

import com.tallerpicado.domain.DetalleFactura;

import java.util.List;
import java.util.Optional;

public interface DetalleFacturaService {
    List<DetalleFactura> obtenerTodos();
    List<DetalleFactura> obtenerPorFacturaId(Long facturaId); // ¡Método añadido!
    Optional<DetalleFactura> obtenerPorId(Long id);
    DetalleFactura guardar(DetalleFactura detalle);
    DetalleFactura actualizar(Long id, DetalleFactura detalle);
    void eliminar(Long id);
}
