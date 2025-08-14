package com.tallerpicado.service;

import com.tallerpicado.domain.Produccion;

import java.util.List;

public interface ProduccionService {

    List<Produccion> listarTodas();
    void insertarProduccion(Long idMaquina, Long idOrden);
    void actualizarProduccion(Produccion produccion);
    void eliminarProduccion(Long idProduccion);
    List<Produccion> buscarPorEstado(String patronEstado);
    List<Produccion> buscarPorNombreMaquina(String patronNombre);
    List<Produccion> buscarPorOrden(Long idOrden);
}
