package com.tallerpicado.controller; // O com.tallerpicado.web.controller

import com.tallerpicado.domain.Produccion;
import com.tallerpicado.domain.Maquinaria; // Necesario para el select de maquinaria
import com.tallerpicado.domain.OrdenDeTrabajo; // Necesario para el select de órdenes de trabajo
import com.tallerpicado.service.ProduccionService;
import com.tallerpicado.service.MaquinariaService; // Necesitarás un MaquinariaService
import com.tallerpicado.service.OrdenDeTrabajoService; // Necesitarás un OrdenDeTrabajoService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // ¡Importante: @Controller!
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/produccion-web") // URL para la vista web de producción
public class ProduccionWebController {

    @Autowired
    private ProduccionService produccionService;

    @Autowired
    private MaquinariaService maquinariaService; // Para obtener la lista de maquinaria
    
    @Autowired
    private OrdenDeTrabajoService ordenDeTrabajoService; // Para obtener la lista de órdenes de trabajo

    // Muestra la lista de todos los registros de producción
    @GetMapping
    public String listarProducciones(Model model) {
        List<Produccion> producciones = produccionService.obtenerTodas();
        List<Maquinaria> maquinarias = maquinariaService.obtenerTodas(); // Carga todas las maquinarias
        List<OrdenDeTrabajo> ordenes = ordenDeTrabajoService.obtenerTodas(); // Carga todas las órdenes

        model.addAttribute("producciones", producciones); // Pasa la lista de producciones
        model.addAttribute("produccion", new Produccion()); // Objeto Produccion vacío para el formulario modal
        model.addAttribute("maquinarias", maquinarias); // Pasa la lista de maquinarias para el select
        model.addAttribute("ordenes", ordenes); // Pasa la lista de órdenes de trabajo para el select

        return "produccion"; // Nombre de la plantilla HTML (produccion.html)
    }

    // Guarda un nuevo registro de producción (desde el modal)
    @PostMapping("/guardar")
    public String guardarProduccion(@ModelAttribute Produccion produccion, RedirectAttributes redirectAttributes) {
        // Cargar el objeto Maquinaria completo si el formulario solo envía el ID
        if (produccion.getMaquina() != null && produccion.getMaquina().getId() != null) {
            maquinariaService.obtenerPorId(produccion.getMaquina().getId()).ifPresent(produccion::setMaquina);
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Debe seleccionar una máquina.");
            return "redirect:/produccion-web";
        }

        // Cargar el objeto OrdenDeTrabajo completo si el formulario solo envía el ID
        if (produccion.getOrden() != null && produccion.getOrden().getId() != null) {
            ordenDeTrabajoService.obtenerPorId(produccion.getOrden().getId()).ifPresent(produccion::setOrden);
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Debe seleccionar una orden de trabajo.");
            return "redirect:/produccion-web";
        }

        produccionService.guardar(produccion);
        redirectAttributes.addFlashAttribute("mensajeExito", "Registro de producción guardado exitosamente!");
        return "redirect:/produccion-web"; // Redirige de vuelta a la lista de producciones
    }

    // Métodos para editar y eliminar (se pueden añadir más tarde)
    // @GetMapping("/editar/{id}") ...
    // @PostMapping("/actualizar") ...
    // @GetMapping("/eliminar/{id}") ...
}