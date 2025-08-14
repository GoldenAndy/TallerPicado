package com.tallerpicado.controller;

import com.tallerpicado.domain.Proveedor;
import com.tallerpicado.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;


    @GetMapping
    public String listarProveedores(Model model) {
        List<Proveedor> proveedores = proveedorService.obtenerTodos();
        model.addAttribute("proveedores", proveedores);
        return "proveedores";
    }


    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor, RedirectAttributes redirect) {
        try {
            proveedorService.guardar(proveedor);
            redirect.addFlashAttribute("success", "Proveedor registrado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String m = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            if (m.contains("ORA-20065") || m.contains("ORA-00001")) { 
                redirect.addFlashAttribute("errorNuevoProv", "Ya existe un proveedor con ese nombre.");
                redirect.addFlashAttribute("openNuevoProv", true);
            } else if (m.contains("ORA-20062")) {
                redirect.addFlashAttribute("errorNuevoProv", "El correo no tiene un formato válido.");
                redirect.addFlashAttribute("openNuevoProv", true);
            } else {
                redirect.addFlashAttribute("error", "No se pudo registrar el proveedor.");
            }
            redirect.addFlashAttribute("provNombreIntento", proveedor.getNombreEmpresa());
            redirect.addFlashAttribute("provCelularIntento", proveedor.getCelular());
            redirect.addFlashAttribute("provCorreoIntento", proveedor.getCorreo());
        }
        return "redirect:/proveedores";
    }


    @PostMapping("/actualizar/{id}")
    public String actualizarProveedor(@PathVariable Long id,
                                      @ModelAttribute Proveedor proveedor,
                                      RedirectAttributes redirect) {
        try {
            proveedorService.actualizar(id, proveedor);
            redirect.addFlashAttribute("success", "Proveedor actualizado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String m = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            if (m.contains("ORA-20066") || m.contains("ORA-00001")) { 
                redirect.addFlashAttribute("errorEditarProv", "No se puede renombrar: ya existe un proveedor con ese nombre.");
                redirect.addFlashAttribute("openEditarProv", true);
            } else if (m.contains("ORA-20062")) { 
                redirect.addFlashAttribute("errorEditarProv", "El correo no tiene un formato válido.");
                redirect.addFlashAttribute("openEditarProv", true);
            } else if (m.contains("ORA-20067")) { 
                redirect.addFlashAttribute("error", "El proveedor indicado no existe.");
            } else {
                redirect.addFlashAttribute("error", "No se pudo actualizar el proveedor.");
            }
            redirect.addFlashAttribute("editIdProv", id);
            redirect.addFlashAttribute("editNombreProv", proveedor.getNombreEmpresa());
            redirect.addFlashAttribute("editCelularProv", proveedor.getCelular());
            redirect.addFlashAttribute("editCorreoProv", proveedor.getCorreo());
        }
        return "redirect:/proveedores";
    }



    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            proveedorService.eliminar(id);
            redirect.addFlashAttribute("success", "Proveedor eliminado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String msg = (e.getMostSpecificCause() != null)
                    ? e.getMostSpecificCause().getMessage()
                    : "No se pudo eliminar el proveedor. Inténtelo nuevamente.";
            redirect.addFlashAttribute("error", msg);
        }
        return "redirect:/proveedores";
    }


    @GetMapping("/buscar")
    public String buscarProveedores(@RequestParam("nombre") String nombre, Model model) {
        List<Proveedor> resultados = proveedorService.buscarPorNombreRegex(nombre);
        model.addAttribute("proveedores", resultados);
        return "proveedores";
    }
}
