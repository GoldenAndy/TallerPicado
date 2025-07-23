package com.tallerpicado.service;

import com.tallerpicado.domain.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    List<Empleado> obtenerTodos();  // SP_LISTAR_EMPLEADOS

    Optional<Empleado> obtenerPorId(Long id);

    Empleado guardar(Empleado empleado);  // SP_INSERTAR_EMPLEADO

    Empleado actualizar(Long id, Empleado empleado);  // SP_ACTUALIZAR_EMPLEADO

    void eliminar(Long id);  // SP_ELIMINAR_EMPLEADO

    List<Empleado> buscarPorNombre(String patron);  // FN_BUSCAR_EMPLEADO_POR_NOMBRE

    List<Empleado> filtrarPorPuesto(Long idPuesto);  // FN_BUSCAR_EMPLEADO_POR_PUESTO

    List<Empleado> filtrarPorProveedor(Long idProveedor);  // FN_BUSCAR_EMPLEADO_POR_PROVEEDOR

    List<Empleado> filtrarPorEstado(String estado);  // FN_BUSCAR_EMPLEADO_POR_ESTADO
    
    List<Empleado> filtrarMultiples(String nombre, Long idPuesto, Long idProveedor, String estado);
    
    Optional<Empleado> buscarPorNombreExacto(String nombre);

}
