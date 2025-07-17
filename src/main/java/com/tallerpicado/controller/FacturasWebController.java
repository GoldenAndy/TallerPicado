package com.tallerpicado.controller;

import com.tallerpicado.domain.Factura;
import com.tallerpicado.service.FacturaService;
import com.tallerpicado.service.ClienteService;
import com.tallerpicado.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/facturas-web")
public class FacturasWebController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ClienteService clienteService; // Necesitarás un ClienteService
    
    @Autowired
    private EmpleadoService empleadoService; // Necesitarás un EmpleadoService

    // Muestra la lista de facturas y prepara datos para el formulario modal
    @GetMapping
    public String listarFacturas(Model model) {
        List<Factura> facturas = facturaService.obtenerTodas();
        model.addAttribute("facturas", facturas);
        model.addAttribute("factura", new Factura()); // Objeto Factura vacío para el formulario modal
        model.addAttribute("clientes", clienteService.obtenerTodos()); // Carga clientes para el select
        model.addAttribute("empleados", empleadoService.obtenerTodos()); // Carga empleados para el select
        return "facturas"; // Retorna la plantilla principal facturas.html
    }

    // Muestra el formulario para editar una factura existente (esto aún podría ser una página separada)
    // Si quieres que la edición también sea un modal, necesitaríamos más lógica JS para cargar datos.
    // Por ahora, mantenemos esta ruta para edición como una página separada.
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarFactura(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Factura> facturaOptional = facturaService.obtenerPorId(id);
        if (facturaOptional.isPresent()) {
            model.addAttribute("factura", facturaOptional.get());
            model.addAttribute("clientes", clienteService.obtenerTodos());
            model.addAttribute("empleados", empleadoService.obtenerTodos());
            return "factura-form"; // Asumiendo que tienes un factura-form.html para la edición
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Factura no encontrada para editar.");
            return "redirect:/facturas-web";
        }
    }

    // Guarda una nueva factura (desde el modal) o actualiza una existente
    @PostMapping("/guardar")
    public String guardarFactura(@ModelAttribute Factura factura, RedirectAttributes redirectAttributes) {
        // Antes de guardar, asegúrate de que Cliente y Empleado estén completamente cargados
        // El formulario solo envía el ID, así que necesitamos cargar la entidad completa
        if (factura.getCliente() != null && factura.getCliente().getId() != null) {
            clienteService.obtenerPorId(factura.getCliente().getId()).ifPresent(factura::setCliente);
        }
        if (factura.getEmpleado() != null && factura.getEmpleado().getId() != null) {
            empleadoService.obtenerPorId(factura.getEmpleado().getId()).ifPresent(factura::setEmpleado);
        }

        facturaService.guardar(factura);
        redirectAttributes.addFlashAttribute("mensajeExito", "Factura guardada exitosamente!");
        return "redirect:/facturas-web"; // Redirige de vuelta a la lista de facturas
    }

    // Actualiza una factura existente (maneja el POST del formulario de edición, si es una página separada)
    @PostMapping("/actualizar")
    public String actualizarFactura(@ModelAttribute Factura factura, RedirectAttributes redirectAttributes) {
        // Asegúrate de que los objetos Cliente y Empleado estén correctamente asociados
        if (factura.getCliente() != null && factura.getCliente().getId() != null) {
            clienteService.obtenerPorId(factura.getCliente().getId()).ifPresent(factura::setCliente);
        }
        if (factura.getEmpleado() != null && factura.getEmpleado().getId() != null) {
            empleadoService.obtenerPorId(factura.getEmpleado().getId()).ifPresent(factura::setEmpleado);
        }
        
        facturaService.actualizar(factura.getId(), factura);
        redirectAttributes.addFlashAttribute("mensajeExito", "Factura actualizada exitosamente!");
        return "redirect:/facturas-web";
    }

    // Elimina una factura
    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        facturaService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Factura eliminada exitosamente!");
        return "redirect:/facturas-web";
    }
}