package com.tallerpicado.controller;

import com.tallerpicado.domain.Mantenimiento;
import com.tallerpicado.service.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mantenimientos")
@CrossOrigin(origins = "*")
public class MantenimientoController {

    @Autowired
    private MantenimientoService mantenimientoService;

    @GetMapping
    public List<Mantenimiento> listar() {
        return mantenimientoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<Mantenimiento> obtener(@PathVariable Long id) {
        return mantenimientoService.obtenerPorId(id);
    }

    @PostMapping
    public Mantenimiento crear(@RequestBody Mantenimiento mantenimiento) {
        return mantenimientoService.guardar(mantenimiento);
    }

    @PutMapping("/{id}")
    public Mantenimiento actualizar(@PathVariable Long id, @RequestBody Mantenimiento mantenimiento) {
        return mantenimientoService.actualizar(id, mantenimiento);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        mantenimientoService.eliminar(id);
    }
}
