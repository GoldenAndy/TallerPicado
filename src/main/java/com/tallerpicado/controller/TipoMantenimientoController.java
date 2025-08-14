package com.tallerpicado.controller;

import com.tallerpicado.domain.TipoMantenimiento;
import com.tallerpicado.service.TipoMantenimientoService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tipos-mantenimiento")
public class TipoMantenimientoController {

    @Autowired
    private TipoMantenimientoService tipoService;

    @GetMapping
    public String mostrarTipos(Model model) {
        List<TipoMantenimiento> tipos = tipoService.obtenerTodos();
        model.addAttribute("tipos", tipos);
        return "tipos-mantenimiento";
    }


    @PostMapping("/guardar")
    public String guardarTipo(@ModelAttribute TipoMantenimiento tipo, RedirectAttributes redirect) {
        try {
            tipoService.guardar(tipo);
            redirect.addFlashAttribute("success", "Tipo de mantenimiento registrado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String m = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            String mensaje;
            if (m.contains("ORA-20120") || m.contains("ORA-20121")) { 
                mensaje = m;
            } else if (m.contains("ORA-00001")) { 
                mensaje = m.contains("UX_TIPO_MANT_NOMBRE_NORM")
                        ? "Ya existe un tipo con ese nombre."
                        : "Ese nombre ya está registrado.";
            } else {
                mensaje = "No se pudo registrar el tipo de mantenimiento.";
            }
            redirect.addFlashAttribute("errorNuevoTipo", mensaje);
            redirect.addFlashAttribute("openNuevoTipo", true);
            redirect.addFlashAttribute("tipoNombreIntento", tipo.getNombre());
        }
        return "redirect:/tipos-mantenimiento";
    }


    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        tipoService.obtenerPorId(id).ifPresent(t -> model.addAttribute("tipoMantenimiento", t));
        model.addAttribute("tipos", tipoService.obtenerTodos());
        return "tipos-mantenimiento";
    }


    @PostMapping("/actualizar/{id}")
    public String actualizarTipo(@PathVariable Long id,
            @ModelAttribute TipoMantenimiento tipo,
            RedirectAttributes redirect) {
        try {
            tipoService.actualizar(id, tipo);
            redirect.addFlashAttribute("success", "Tipo de mantenimiento actualizado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String m = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            String mensaje;
            if (m.contains("ORA-20122")) { 
                mensaje = m;
            } else if (m.contains("ORA-20123")) { 
                redirect.addFlashAttribute("error", "El tipo de mantenimiento indicado no existe.");
                return "redirect:/tipos-mantenimiento";
            } else if (m.contains("ORA-20120")) { 
                mensaje = m;
            } else if (m.contains("ORA-00001")) { 
                mensaje = m.contains("UX_TIPO_MANT_NOMBRE_NORM")
                        ? "Ya existe un tipo con ese nombre."
                        : "Ese nombre ya está registrado.";
            } else {
                mensaje = "No se pudo actualizar el tipo de mantenimiento.";
            }
            redirect.addFlashAttribute("errorEditarTipo", mensaje);
            redirect.addFlashAttribute("openEditarTipo", true);
            // Rehidratar campos del modal "Editar"
            redirect.addFlashAttribute("editIdTipo", id);
            redirect.addFlashAttribute("editNombreTipo", tipo.getNombre());
        }
        return "redirect:/tipos-mantenimiento";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTipo(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            tipoService.eliminar(id); 
            redirect.addFlashAttribute("success", "Tipo de mantenimiento eliminado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            if (msg.contains("ORA-20131")) {      
                redirect.addFlashAttribute("error", msg);
            } else if (msg.contains("ORA-20133")) {
                redirect.addFlashAttribute("error", "El tipo de mantenimiento indicado no existe.");
            } else if (msg.contains("ORA-02292")) { 
                redirect.addFlashAttribute("error", "No es posible eliminar: el tipo tiene registros relacionados.");
            } else {
                redirect.addFlashAttribute("error", "No se pudo eliminar el tipo de mantenimiento.");
            }
        }
        return "redirect:/tipos-mantenimiento";
    }

    @GetMapping("/buscar")
    public String buscarTipos(@RequestParam("nombre") String nombre, Model model) {
        List<TipoMantenimiento> tipos = tipoService.buscarPorNombre(nombre);
        model.addAttribute("tipos", tipos != null ? tipos : new ArrayList<>());
        model.addAttribute("usuario", "TALLERPICADO");
        return "tipos-mantenimiento";
    }
    
}
