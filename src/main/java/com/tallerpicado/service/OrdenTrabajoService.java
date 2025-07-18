package com.tallerpicado.service;

import com.tallerpicado.domain.OrdenTrabajo;

import java.util.List;
import java.util.Optional;

public interface OrdenTrabajoService {
    List<OrdenTrabajo> obtenerTodas();
    Optional<OrdenTrabajo> obtenerPorId(Long id);
    OrdenTrabajo guardar(OrdenTrabajo orden);
    OrdenTrabajo actualizar(Long id, OrdenTrabajo orden);
    void eliminar(Long id);
}
