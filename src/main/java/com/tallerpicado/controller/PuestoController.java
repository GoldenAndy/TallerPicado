package com.tallerpicado.controller;

import com.tallerpicado.domain.Puesto;
import com.tallerpicado.service.PuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/puestos")
@RequiredArgsConstructor
public class PuestoController {

    private final PuestoService puestoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("puestos", puestoService.listarPuestos());
        return "puestos";
    }

    @GetMapping("/buscar")
    public String buscarPorNombre(@RequestParam("nombre") String nombre, Model model) {
        List<Puesto> encontrados = puestoService.buscarPorNombre(nombre);
        model.addAttribute("puestos", encontrados);
        return "puestos";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam("nombre") String nombre, Model model) {
        try {
            puestoService.guardarPuesto(nombre);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("puestos", puestoService.listarPuestos());
            return "puestos"; // Regresa con el error mostrado
        }

        return "redirect:/puestos";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @RequestParam("nombre") String nombre) {
        puestoService.actualizarPuesto(id, nombre);
        return "redirect:/puestos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        puestoService.eliminarPuesto(id);
        return "redirect:/puestos";
    }
}
