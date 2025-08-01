package com.tallerpicado.controller;

import com.tallerpicado.domain.Mantenimiento;
import com.tallerpicado.service.MantenimientoService;
import com.tallerpicado.service.TipoMantenimientoService;
import com.tallerpicado.service.MaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    @Autowired
    private MantenimientoService mantenimientoService;

    @Autowired
    private TipoMantenimientoService tipoMantenimientoService;

    @Autowired
    private MaquinariaService maquinariaService;

    // Mostrar mantenimientos
    @GetMapping
    public String vistaMantenimientos(Model model) {
        model.addAttribute("mantenimientos", mantenimientoService.obtenerTodos());
        model.addAttribute("tipos", tipoMantenimientoService.obtenerTodos());
        model.addAttribute("maquinas", maquinariaService.obtenerTodas());
        return "mantenimientos";
    }

    // Guardar nuevo mantenimiento
    @PostMapping("/guardar")
    public String guardarMantenimiento(@ModelAttribute Mantenimiento mantenimiento) {
        mantenimientoService.guardar(mantenimiento);
        return "redirect:/mantenimientos";
    }

    // Actualizar mantenimiento existente
    @PostMapping("/actualizar/{id}")
    public String actualizarMantenimiento(@PathVariable Long id, @ModelAttribute Mantenimiento mantenimiento) {
        mantenimientoService.actualizar(id, mantenimiento);
        return "redirect:/mantenimientos";
    }

    // Eliminar mantenimiento por ID
    @GetMapping("/eliminar/{id}")
    public String eliminarMantenimiento(@PathVariable Long id) {
        mantenimientoService.eliminar(id);
        return "redirect:/mantenimientos";
    }
}
