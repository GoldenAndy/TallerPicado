package com.tallerpicado.service;

import java.util.List;
import java.util.Optional;

import com.tallerpicado.domain.Maquinaria;

public interface MaquinariaService {
    List<Maquinaria> obtenerTodas();
    Optional<Maquinaria> obtenerPorId(Long id);
    Maquinaria guardar(Maquinaria maquinaria);
    Maquinaria actualizar(Long id, Maquinaria maquinaria);
    void eliminar(Long id);
    List<Maquinaria> buscarPorNombre(String patron);
    List<Maquinaria> obtenerPorEstado(String estado); /*PARA PRODUCCIÓN — Andrés*/

}
