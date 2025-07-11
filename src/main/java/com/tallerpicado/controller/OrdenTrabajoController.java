package com.tallerpicado.controller;

import com.tallerpicado.domain.OrdenTrabajo;
import com.tallerpicado.service.OrdenTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*")
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @GetMapping
    public List<OrdenTrabajo> listar() {
        return ordenTrabajoService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Optional<OrdenTrabajo> obtener(@PathVariable Long id) {
        return ordenTrabajoService.obtenerPorId(id);
    }

    @PostMapping
    public OrdenTrabajo crear(@RequestBody OrdenTrabajo orden) {
        return ordenTrabajoService.guardar(orden);
    }

    @PutMapping("/{id}")
    public OrdenTrabajo actualizar(@PathVariable Long id, @RequestBody OrdenTrabajo orden) {
        return ordenTrabajoService.actualizar(id, orden);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        ordenTrabajoService.eliminar(id);
    }
}
