package com.tallerpicado.controller;

import com.tallerpicado.domain.DetalleFactura;
import com.tallerpicado.service.DetalleFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detallefactura")
@CrossOrigin(origins = "*")
public class DetalleFacturaController {

    @Autowired
    private DetalleFacturaService service;

    @GetMapping
    public List<DetalleFactura> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<DetalleFactura> obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public DetalleFactura crear(@RequestBody DetalleFactura detalle) {
        return service.guardar(detalle);
    }

    @PutMapping("/{id}")
    public DetalleFactura actualizar(@PathVariable Long id, @RequestBody DetalleFactura detalle) {
        return service.actualizar(id, detalle);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
