package com.tallerpicado.service;

import com.tallerpicado.domain.Articulo;

import java.util.List;
import java.util.Optional;

public interface ArticuloService {
    List<Articulo> obtenerTodos();
    Optional<Articulo> obtenerPorId(Long id);
    Articulo guardar(Articulo articulo);
    Articulo actualizar(Long id, Articulo articulo);
    void eliminar(Long id);
}
