package com.tallerpicado.service;

import java.util.List;
import java.util.Optional;

import com.tallerpicado.domain.Articulo;

public interface ArticuloService {
    List<Articulo> obtenerTodos();
    Optional<Articulo> obtenerPorId(Long id);
    Articulo guardar(Articulo articulo);
    Articulo actualizar(Long id, Articulo articulo);
    void eliminar(Long id);
    List<Articulo> buscarPorNombre(String patron);
}