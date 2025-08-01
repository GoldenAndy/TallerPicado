package com.tallerpicado.service;

import com.tallerpicado.domain.TipoMantenimiento;
import java.util.List;
import java.util.Optional;

public interface TipoMantenimientoService {
    List<TipoMantenimiento> obtenerTodos();
    Optional<TipoMantenimiento> obtenerPorId(Long id);
    TipoMantenimiento guardar(TipoMantenimiento tipo);
    TipoMantenimiento actualizar(Long id, TipoMantenimiento tipo);
    void eliminar(Long id);

    List<TipoMantenimiento> buscarPorNombre(String nombre);
}
