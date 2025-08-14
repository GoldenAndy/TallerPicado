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

    List<Empleado> buscarPorNombre(String patron); 

    List<Empleado> filtrarPorPuesto(Long idPuesto);  

    List<Empleado> filtrarPorProveedor(Long idProveedor);  

    List<Empleado> filtrarPorEstado(String estado); 
    
    List<Empleado> filtrarMultiples(String nombre, Long idPuesto, Long idProveedor, String estado);
    
    Optional<Empleado> buscarPorNombreExacto(String nombre);

}
