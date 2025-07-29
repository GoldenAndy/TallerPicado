package com.tallerpicado.controller;

import com.tallerpicado.domain.TipoMantenimiento;
import com.tallerpicado.service.TipoMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tipos-mantenimiento")
public class TipoMantenimientoController {

    @Autowired
    private TipoMantenimientoService tipoService;

    @GetMapping
    public String mostrarTipos(Model model) {
        List<TipoMantenimiento> tipos = tipoService.obtenerTodos();
        model.addAttribute("tipos", tipos);
        return "tipos-mantenimiento";  // <-- nombre exacto de tu html sin extensión
    }

    @PostMapping("/guardar")
    public String guardarTipo(@ModelAttribute TipoMantenimiento tipo) {
        tipoService.guardar(tipo);
        return "redirect:/tipos-mantenimiento";
    }

    // Opcional: métodos para actualizar y eliminar usando formularios Thymeleaf
}
