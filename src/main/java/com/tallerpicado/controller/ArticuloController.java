package com.tallerpicado.controller;

import com.tallerpicado.domain.Articulo;
import com.tallerpicado.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/articulos")
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    @GetMapping("")
    public String listarArticulos(Model model) {
        model.addAttribute("articulos", articuloService.obtenerTodos());
        return "articulos";
    }

    @PostMapping("/guardar")
    public String guardar(Articulo articulo) {
        articuloService.guardar(articulo);
        return "redirect:/articulos";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, Articulo articulo) {
        articuloService.actualizar(id, articulo);
        return "redirect:/articulos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        articuloService.eliminar(id);
        return "redirect:/articulos";
    }
}
