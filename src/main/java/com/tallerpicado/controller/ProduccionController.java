package com.tallerpicado.controller;

import com.tallerpicado.domain.Produccion;
import com.tallerpicado.service.ProduccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produccion")
@CrossOrigin(origins = "*")
public class ProduccionController {

    @Autowired
    private ProduccionService produccionService;

    @GetMapping
    public List<Produccion> listar() {
        return produccionService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Optional<Produccion> obtener(@PathVariable Long id) {
        return produccionService.obtenerPorId(id);
    }

    @PostMapping
    public Produccion crear(@RequestBody Produccion produccion) {
        return produccionService.guardar(produccion);
    }

    @PutMapping("/{id}")
    public Produccion actualizar(@PathVariable Long id, @RequestBody Produccion produccion) {
        return produccionService.actualizar(id, produccion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        produccionService.eliminar(id);
    }
}
