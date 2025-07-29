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

    @GetMapping
    public String vistaMantenimientos(Model model) {
        model.addAttribute("mantenimientos", mantenimientoService.obtenerTodos());
        model.addAttribute("tipos", tipoMantenimientoService.obtenerTodos());
        model.addAttribute("maquinas", maquinariaService.obtenerTodas());
        return "mantenimientos";
    }

    @PostMapping("/guardar")
    public String guardarMantenimiento(@ModelAttribute Mantenimiento mantenimiento) {
        mantenimientoService.guardar(mantenimiento);
        return "redirect:/mantenimientos";
    }
}
