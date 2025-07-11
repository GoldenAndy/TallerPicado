package com.tallerpicado.service;

import com.tallerpicado.domain.ProveedorEmpleado;

import java.util.List;

public interface ProveedorEmpleadoService {
    List<ProveedorEmpleado> listarTodos();
    ProveedorEmpleado asignarEmpleado(ProveedorEmpleado relacion);
    void eliminarRelacion(Long idProveedor, Long idEmpleado);
}
