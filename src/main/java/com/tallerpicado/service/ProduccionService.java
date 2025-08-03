package com.tallerpicado.service;

import com.tallerpicado.domain.Produccion;

import java.util.List;

public interface ProduccionService {

    // Listado general
    List<Produccion> listarTodas();

    // Insertar nueva producción (verifica máquina DISPONIBLE)
    void insertarProduccion(Long idMaquina, Long idOrden);

    // Actualizar una producción
    void actualizarProduccion(Produccion produccion);

    // Eliminar una producción
    void eliminarProduccion(Long idProduccion);

    // Buscar por estado usando expresión regular
    List<Produccion> buscarPorEstado(String patronEstado);

    // Buscar por nombre de máquina con expresión regular
    List<Produccion> buscarPorNombreMaquina(String patronNombre);

    // Buscar todas las producciones asociadas a una orden específica
    List<Produccion> buscarPorOrden(Long idOrden);
}
