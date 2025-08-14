package com.tallerpicado.controller;

import com.tallerpicado.domain.Puesto;
import com.tallerpicado.service.PuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String guardar(@RequestParam("nombre") String nombre, RedirectAttributes redirect) {
        try {
            puestoService.guardarPuesto(nombre);
            redirect.addFlashAttribute("success", "Puesto registrado correctamente.");
        } catch (DataAccessException e) {
            String m = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            if (m.contains("ORA-20055") || m.contains("ORA-00001")) {
                redirect.addFlashAttribute("errorNuevo", "Ya existe un puesto con ese nombre.");
                redirect.addFlashAttribute("openNuevo", true);
                redirect.addFlashAttribute("nombreIntento", nombre);
            } else {
                redirect.addFlashAttribute("error", "No se pudo registrar el puesto. Inténtelo nuevamente.");
            }
        }
        return "redirect:/puestos";
    }


    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @RequestParam("nombre") String nombre, RedirectAttributes redirect) {
        try {
            puestoService.actualizarPuesto(id, nombre);
            redirect.addFlashAttribute("success", "Puesto actualizado correctamente.");
        } catch (DataAccessException e) {
            String m = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            if (m.contains("ORA-20056") || m.contains("ORA-00001")) {
                redirect.addFlashAttribute("errorEditar", "No se puede renombrar: ya existe un puesto con ese nombre.");
                redirect.addFlashAttribute("openEditar", true);
                redirect.addFlashAttribute("editId", id);
                redirect.addFlashAttribute("editNombre", nombre);
            } else if (m.contains("ORA-20057")) {
                redirect.addFlashAttribute("error", "El puesto indicado no existe.");
            } else {
                redirect.addFlashAttribute("error", "No se pudo actualizar el puesto. Inténtelo nuevamente.");
            }
        }
        return "redirect:/puestos";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            puestoService.eliminarPuesto(id);
            redirect.addFlashAttribute("success", "Puesto eliminado correctamente.");
        } catch (DataAccessException e) {
            String mensajeError = e.getMostSpecificCause() != null
                    ? e.getMostSpecificCause().getMessage()
                    : "No se pudo eliminar el puesto. Inténtelo nuevamente.";
            redirect.addFlashAttribute("error", mensajeError);
        }
        return "redirect:/puestos";
    }

}
