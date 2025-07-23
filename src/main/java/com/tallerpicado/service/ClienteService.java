package com.tallerpicado.service;

import com.tallerpicado.domain.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> obtenerTodos();
    Optional<Cliente> obtenerPorId(Long id);
    Cliente guardar(Cliente cliente);
    Cliente actualizar(Long id, Cliente cliente);
    void eliminar(Long id);
    List<Cliente> buscarPorNombre(String patron);
    Optional<Cliente> buscarPorNombreExacto(String nombre);

}
