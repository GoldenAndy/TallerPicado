package com.tallerpicado.controller;

import com.tallerpicado.domain.Maquinaria;
import com.tallerpicado.service.MaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/maquinaria")
public class MaquinariaController {

    @Autowired
    private MaquinariaService maquinariaService;

    @GetMapping("")  
    public String listarMaquinaria(Model model) {
        model.addAttribute("maquinas", maquinariaService.obtenerTodas());
        return "maquinaria";  
    }

@PostMapping("/guardar")
public String guardar(Maquinaria maquinaria, RedirectAttributes redirect) {
    try {
        maquinariaService.guardar(maquinaria);
        redirect.addFlashAttribute("success", "Máquina registrada correctamente.");
    } catch (org.springframework.dao.DataAccessException e) {
        String m = (e.getMostSpecificCause() != null) ? e.getMostSpecificCause().getMessage() : "";
        String mensaje;
        if (m.contains("ORA-20100")) {                     
            mensaje = "El nombre de la máquina es obligatorio.";
        } else if (m.contains("ORA-20101")) {           
            mensaje = "Ya existe una máquina con ese nombre.";
        } else if (m.contains("ORA-20104")) {              
            mensaje = "Estado inválido. Debe ser: DISPONIBLE, EN_MANTENIMIENTO o FUERA_DE_SERVICIO.";
        } else if (m.contains("ORA-00001")) {               
            mensaje = m.contains("UX_MAQ_NOMBRE_NORM")
                    ? "Ya existe una máquina con ese nombre."
                    : "El nombre ya está registrado.";
        } else {
            mensaje = "No se pudo registrar la máquina.";
        }
        redirect.addFlashAttribute("errorNuevoMaq", mensaje);
        redirect.addFlashAttribute("openNuevoMaq", true);
        // Rehidratar campos del modal "Nuevo"
        redirect.addFlashAttribute("maqNombreIntento", maquinaria.getNombre());
        redirect.addFlashAttribute("maqMarcaIntento", maquinaria.getMarca());
        redirect.addFlashAttribute("maqFechaIntento", String.valueOf(maquinaria.getFechaAdquisicion()));
        redirect.addFlashAttribute("maqEstadoIntento", maquinaria.getEstado());
    }
    return "redirect:/maquinaria";
}


@PostMapping("/actualizar/{id}")
public String actualizar(@PathVariable Long id, Maquinaria maquinaria, RedirectAttributes redirect) {
    try {
        maquinariaService.actualizar(id, maquinaria);
        redirect.addFlashAttribute("success", "Máquina actualizada correctamente.");
    } catch (org.springframework.dao.DataAccessException e) {
        String m = (e.getMostSpecificCause() != null) ? e.getMostSpecificCause().getMessage() : "";
        String mensaje;
        if (m.contains("ORA-20102")) {                       // duplicado en UPDATE (SP)
            mensaje = "No se puede renombrar: ya existe una máquina con ese nombre.";
        } else if (m.contains("ORA-20103")) {                // no existe
            redirect.addFlashAttribute("error", "La máquina indicada no existe.");
            return "redirect:/maquinaria";
        } else if (m.contains("ORA-20100")) {            
            mensaje = "El nombre de la máquina es obligatorio.";
        } else if (m.contains("ORA-20104")) {                // estado inválido
            mensaje = "Estado inválido. Debe ser: DISPONIBLE, EN_MANTENIMIENTO o FUERA_DE_SERVICIO.";
        } else if (m.contains("ORA-00001")) {             
            mensaje = m.contains("UX_MAQ_NOMBRE_NORM")
                    ? "Ya existe una máquina con ese nombre."
                    : "El nombre ya está registrado.";
        } else {
            mensaje = "No se pudo actualizar la máquina.";
        }
        redirect.addFlashAttribute("errorEditarMaq", mensaje);
        redirect.addFlashAttribute("openEditarMaq", true);
        redirect.addFlashAttribute("editIdMaq", id);
        redirect.addFlashAttribute("editNombreMaq", maquinaria.getNombre());
        redirect.addFlashAttribute("editMarcaMaq", maquinaria.getMarca());
        redirect.addFlashAttribute("editFechaMaq", String.valueOf(maquinaria.getFechaAdquisicion()));
        redirect.addFlashAttribute("editEstadoMaq", maquinaria.getEstado());
    }
    return "redirect:/maquinaria";
}


    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            maquinariaService.eliminar(id); 
            redirect.addFlashAttribute("success", "Máquina eliminada correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            if (msg.contains("ORA-20091")) {      
                redirect.addFlashAttribute("error", msg);
            } else if (msg.contains("ORA-20093")) {  
                redirect.addFlashAttribute("error", "La máquina indicada no existe.");
            } else if (msg.contains("ORA-02292")) {
                redirect.addFlashAttribute("error", "No es posible eliminar: la máquina tiene registros relacionados.");
            } else {
                redirect.addFlashAttribute("error", "No se pudo eliminar la máquina.");
            }
        }
        return "redirect:/maquinaria";
    }
}

