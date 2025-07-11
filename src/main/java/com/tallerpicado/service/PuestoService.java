package com.tallerpicado.service;

import com.tallerpicado.domain.Puesto;

import java.util.List;

public interface PuestoService {
    void guardarPuesto(String nombre);
    void actualizarPuesto(Long id, String nombre);
    void eliminarPuesto(Long id);
    List<Puesto> listarPuestos();
    List<Puesto> buscarPorNombre(String patron);
}
