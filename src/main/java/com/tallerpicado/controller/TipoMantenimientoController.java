package com.tallerpicado.controller;

import com.tallerpicado.domain.TipoMantenimiento;
import com.tallerpicado.service.TipoMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipos-mantenimiento")
@CrossOrigin(origins = "*")
public class TipoMantenimientoController {

    @Autowired
    private TipoMantenimientoService service;

    @GetMapping
    public List<TipoMantenimiento> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<TipoMantenimiento> obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public TipoMantenimiento crear(@RequestBody TipoMantenimiento tipo) {
        return service.guardar(tipo);
    }

    @PutMapping("/{id}")
    public TipoMantenimiento actualizar(@PathVariable Long id, @RequestBody TipoMantenimiento tipo) {
        return service.actualizar(id, tipo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
