package com.tallerpicado.service;

import com.tallerpicado.domain.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    
    List<Empleado> obtenerTodos();
    
    Optional<Empleado> obtenerPorId(Long id);
    
    Empleado guardar(Empleado empleado);
    
    Empleado actualizar(Long id, Empleado empleado);
    
    void eliminar(Long id);
}
