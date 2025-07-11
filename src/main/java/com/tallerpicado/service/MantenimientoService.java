package com.tallerpicado.service;

import com.tallerpicado.domain.Mantenimiento;
import java.util.List;
import java.util.Optional;

public interface MantenimientoService {
    List<Mantenimiento> obtenerTodos();
    Optional<Mantenimiento> obtenerPorId(Long id);
    Mantenimiento guardar(Mantenimiento mantenimiento);
    Mantenimiento actualizar(Long id, Mantenimiento mantenimiento);
    void eliminar(Long id);
}
