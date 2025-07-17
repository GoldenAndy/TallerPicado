package com.tallerpicado.service;

import com.tallerpicado.domain.Produccion;

import java.util.List;
import java.util.Optional;

public interface ProduccionService {
    List<Produccion> obtenerTodas();
    Optional<Produccion> obtenerPorId(Long id);
    Produccion guardar(Produccion produccion);
    Produccion actualizar(Long id, Produccion produccion);
    void eliminar(Long id);
}