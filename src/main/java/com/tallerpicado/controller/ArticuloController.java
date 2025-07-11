package com.tallerpicado.controller;

import com.tallerpicado.domain.Articulo;
import com.tallerpicado.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articulos")
@CrossOrigin(origins = "*")
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    @GetMapping
    public List<Articulo> listar() {
        return articuloService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<Articulo> obtener(@PathVariable Long id) {
        return articuloService.obtenerPorId(id);
    }

    @PostMapping
    public Articulo crear(@RequestBody Articulo articulo) {
        return articuloService.guardar(articulo);
    }

    @PutMapping("/{id}")
    public Articulo actualizar(@PathVariable Long id, @RequestBody Articulo articulo) {
        return articuloService.actualizar(id, articulo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        articuloService.eliminar(id);
    }
}
