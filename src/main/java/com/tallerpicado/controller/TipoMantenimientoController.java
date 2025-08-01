package com.tallerpicado.controller;

import com.tallerpicado.domain.TipoMantenimiento;
import com.tallerpicado.service.TipoMantenimientoService;
import java.util.ArrayList;
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

    // Mostrar todos los tipos
    @GetMapping
    public String mostrarTipos(Model model) {
        List<TipoMantenimiento> tipos = tipoService.obtenerTodos();
        model.addAttribute("tipos", tipos);
        return "tipos-mantenimiento";
    }

    // Guardar nuevo tipo
    @PostMapping("/guardar")
    public String guardarTipo(@ModelAttribute TipoMantenimiento tipo) {
        tipoService.guardar(tipo);
        return "redirect:/tipos-mantenimiento";
    }

    // Mostrar formulario de edición con el tipo cargado
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        tipoService.obtenerPorId(id).ifPresent(t -> model.addAttribute("tipoMantenimiento", t));
        model.addAttribute("tipos", tipoService.obtenerTodos());
        return "tipos-mantenimiento";
    }

    // Actualizar tipo existente
    @PostMapping("/actualizar/{id}")
    public String actualizarTipo(@PathVariable Long id, @ModelAttribute TipoMantenimiento tipo) {
        tipoService.actualizar(id, tipo);
        return "redirect:/tipos-mantenimiento";
    }

    // Eliminar tipo
    @GetMapping("/eliminar/{id}")
    public String eliminarTipo(@PathVariable Long id) {
        tipoService.eliminar(id);
        return "redirect:/tipos-mantenimiento";
    }

    // Si algún día reactivas la búsqueda por nombre, este bloque está listo para usar:
    
    @GetMapping("/buscar")
    public String buscarTipos(@RequestParam("nombre") String nombre, Model model) {
        List<TipoMantenimiento> tipos = tipoService.buscarPorNombre(nombre);
        model.addAttribute("tipos", tipos != null ? tipos : new ArrayList<>());
        model.addAttribute("usuario", "TALLERPICADO");
        return "tipos-mantenimiento";
    }
    
}
