package com.tallerpicado.service;

import com.tallerpicado.domain.Maquinaria;
import java.util.List;
import java.util.Optional;

public interface MaquinariaService {
    List<Maquinaria> obtenerTodas();
    Optional<Maquinaria> obtenerPorId(Long id);
    Maquinaria guardar(Maquinaria maquinaria);
    Maquinaria actualizar(Long id, Maquinaria maquinaria);
    void eliminar(Long id);
}
