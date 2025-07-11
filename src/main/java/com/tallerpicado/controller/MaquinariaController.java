package com.tallerpicado.controller;

import com.tallerpicado.domain.Maquinaria;
import com.tallerpicado.service.MaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/maquinaria")
@CrossOrigin(origins = "*")
public class MaquinariaController {

    @Autowired
    private MaquinariaService maquinariaService;

    @GetMapping
    public List<Maquinaria> listar() {
        return maquinariaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Optional<Maquinaria> obtener(@PathVariable Long id) {
        return maquinariaService.obtenerPorId(id);
    }

    @PostMapping
    public Maquinaria crear(@RequestBody Maquinaria maquinaria) {
        return maquinariaService.guardar(maquinaria);
    }

    @PutMapping("/{id}")
    public Maquinaria actualizar(@PathVariable Long id, @RequestBody Maquinaria maquinaria) {
        return maquinariaService.actualizar(id, maquinaria);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        maquinariaService.eliminar(id);
    }
}
