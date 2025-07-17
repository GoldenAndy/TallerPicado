package com.tallerpicado.controller; // O com.tallerpicado.web.controller

import com.tallerpicado.domain.DetalleFactura;
import com.tallerpicado.domain.Factura; // Necesario para obtener la factura principal
import com.tallerpicado.domain.Articulo; // Necesario para el select de artículos
import com.tallerpicado.service.DetalleFacturaService;
import com.tallerpicado.service.FacturaService; // Para obtener la factura principal
import com.tallerpicado.service.ArticuloService; // Para obtener la lista de artículos
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
@RequestMapping("/facturas-web/{facturaId}/detalles") // URL para detalles de una factura específica
public class DetalleFacturaWebController {

    @Autowired
    private DetalleFacturaService detalleFacturaService;

    @Autowired
    private FacturaService facturaService; // Para obtener la factura principal

    @Autowired
    private ArticuloService articuloService; // Para obtener la lista de artículos (productos/servicios)

    // Muestra la vista de detalles para una factura específica
    @GetMapping
    public String listarDetallesFactura(@PathVariable Long facturaId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Factura> facturaOptional = facturaService.obtenerPorId(facturaId);
        if (facturaOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Factura no encontrada.");
            return "redirect:/facturas-web"; // Redirige a la lista de facturas si no existe
        }

        Factura factura = facturaOptional.get();
        List<DetalleFactura> detalles = detalleFacturaService.obtenerPorFacturaId(facturaId);
        List<Articulo> articulos = articuloService.obtenerTodos(); // Carga todos los artículos

        model.addAttribute("factura", factura); // Pasa la factura principal
        model.addAttribute("detallesFactura", detalles); // Pasa la lista de detalles
        model.addAttribute("detalleFactura", new DetalleFactura()); // Objeto vacío para el formulario modal
        model.addAttribute("articulos", articulos); // Pasa la lista de artículos para el select

        return "detallefactura"; // ¡CORREGIDO: Ahora devuelve "detallefactura" sin guion!
    }

    // Guarda un nuevo detalle de factura (desde el modal)
    @PostMapping("/guardar")
    public String guardarDetalleFactura(@PathVariable Long facturaId, @ModelAttribute DetalleFactura detalleFactura, RedirectAttributes redirectAttributes) {
        Optional<Factura> facturaOptional = facturaService.obtenerPorId(facturaId);
        if (facturaOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Factura principal no encontrada para agregar detalle.");
            return "redirect:/facturas-web";
        }

        Factura factura = facturaOptional.get();
        detalleFactura.setFactura(factura); // Asigna la factura principal al detalle

        // Cargar el objeto Articulo completo si el formulario solo envía el ID
        if (detalleFactura.getArticulo() != null && detalleFactura.getArticulo().getId() != null) {
            articuloService.obtenerPorId(detalleFactura.getArticulo().getId()).ifPresent(detalleFactura::setArticulo);
        } else {
             redirectAttributes.addFlashAttribute("mensajeError", "Debe seleccionar un artículo para el detalle.");
             return "redirect:/facturas-web/" + facturaId + "/detalles";
        }

        // Si el precio unitario no viene del formulario (ej. lo tomas del Articulo)
        if (detalleFactura.getPrecioUnitario() == null && detalleFactura.getArticulo() != null) {
            detalleFactura.setPrecioUnitario(detalleFactura.getArticulo().getPrecio());
        }

        // Calcular subtotal antes de guardar (si no se hace en el servicio)
        if (detalleFactura.getCantidad() != null && detalleFactura.getPrecioUnitario() != null) {
            detalleFactura.setSubtotal(detalleFactura.getCantidad() * detalleFactura.getPrecioUnitario());
        }

        detalleFacturaService.guardar(detalleFactura);
        redirectAttributes.addFlashAttribute("mensajeExito", "Detalle de factura guardado exitosamente!");
        return "redirect:/facturas-web/" + facturaId + "/detalles"; // Redirige de vuelta a la vista de detalles
    }

    // Métodos para editar y eliminar detalles (opcional, se pueden añadir más tarde)
    // @GetMapping("/editar/{detalleId}") ...
    // @GetMapping("/eliminar/{detalleId}") ...
}
