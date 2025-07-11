package com.tallerpicado.service;

import com.tallerpicado.domain.Factura;

import java.util.List;
import java.util.Optional;

public interface FacturaService {
    List<Factura> obtenerTodas();
    Optional<Factura> obtenerPorId(Long id);
    Factura guardar(Factura factura);
    Factura actualizar(Long id, Factura factura);
    void eliminar(Long id);
}
