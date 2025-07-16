package com.tallerpicado.controller;

import com.tallerpicado.domain.Maquinaria;
import com.tallerpicado.service.MaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/maquinaria")
public class MaquinariaVistaController {

    @Autowired
    private MaquinariaService maquinariaService;

    @GetMapping("")  // O @GetMapping("/")
    public String listarMaquinaria(Model model) {
        model.addAttribute("maquinas", maquinariaService.obtenerTodas());
        return "maquinaria";  // Nombre de tu template Thymeleaf
    }

    @PostMapping("/guardar")
    public String guardar(Maquinaria maquinaria) {
        maquinariaService.guardar(maquinaria);
        return "redirect:/maquinaria";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, Maquinaria maquinaria) {
        maquinariaService.actualizar(id, maquinaria);
        return "redirect:/maquinaria";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        maquinariaService.eliminar(id);
        return "redirect:/maquinaria";
    }
}

