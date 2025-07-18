package com.tallerpicado.service;

import com.tallerpicado.domain.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> obtenerTodos();
    Optional<Proveedor> obtenerPorId(Long id);
    Proveedor guardar(Proveedor proveedor);
    Proveedor actualizar(Long id, Proveedor proveedor);
    void eliminar(Long id);
    List<Proveedor> buscarPorNombreRegex(String patron);
}
