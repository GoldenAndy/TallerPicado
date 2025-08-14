package com.tallerpicado.controller;

import com.tallerpicado.domain.DetalleFactura;
import com.tallerpicado.domain.Factura;
import com.tallerpicado.service.ArticuloService;
import com.tallerpicado.service.DetalleFacturaService;
import com.tallerpicado.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/facturas/detalles")
@RequiredArgsConstructor
public class DetalleFacturaController {

    private final DetalleFacturaService detalleFacturaService;
    private final FacturaService facturaService; 
    private final ArticuloService articuloService; 

    // Utilidad
    private String redirectTo(Long idFactura) {
        return "redirect:/facturas/detalles/" + idFactura;
    }


    @GetMapping("/{idFactura}/buscar")
    public String buscar(@PathVariable Long idFactura,
                         @RequestParam String patron,
                         Model model,
                         RedirectAttributes ra) {
        Optional<Factura> facturaOpt = facturaService.obtenerPorId(idFactura);
        if (facturaOpt.isEmpty()) {
            ra.addFlashAttribute("msgErr", "La factura " + idFactura + " no existe.");
            return "redirect:/facturas";
        }

        List<DetalleFactura> detalles = detalleFacturaService.buscarPorArticulo(idFactura, patron);

        model.addAttribute("factura", facturaOpt.get());
        model.addAttribute("detalles", detalles);
        model.addAttribute("articulos", articuloService.obtenerTodos());
        model.addAttribute("idFactura", idFactura);
        model.addAttribute("patron", patron);

        return "detallefactura";
    }


    @PostMapping("/{idFactura}/crear")
    public String crear(@PathVariable Long idFactura,
                        @RequestParam Long idArticulo,
                        @RequestParam Integer cantidad,
                        RedirectAttributes ra) {
        try {
            DetalleFactura d = new DetalleFactura();
            d.setIdFactura(idFactura);
            d.setIdArticulo(idArticulo);
            d.setCantidad(cantidad);

            Long nuevoId = detalleFacturaService.insertar(d);
            ra.addFlashAttribute("msgOk", "Detalle creado (ID " + nuevoId + ").");
        } catch (DataAccessException ex) {
            ra.addFlashAttribute("msgErr", humanizarErrorOracle(ex));
        }
        return redirectTo(idFactura);
    }


    @PostMapping("/{idFactura}/actualizar/{idDetalle}")
    public String actualizar(@PathVariable Long idFactura,
                             @PathVariable Long idDetalle,
                             @RequestParam Long idArticulo,
                             @RequestParam Integer cantidad,
                             RedirectAttributes ra) {
        try {
            DetalleFactura d = new DetalleFactura();
            d.setId(idDetalle);
            d.setIdArticulo(idArticulo);
            d.setCantidad(cantidad);

            detalleFacturaService.actualizar(d);
            ra.addFlashAttribute("msgOk", "Detalle actualizado (ID " + idDetalle + ").");
        } catch (DataAccessException ex) {
            ra.addFlashAttribute("msgErr", humanizarErrorOracle(ex));
        }
        return redirectTo(idFactura);
    }


    @PostMapping("/{idFactura}/eliminar/{idDetalle}")
    public String eliminar(@PathVariable Long idFactura,
                           @PathVariable Long idDetalle,
                           RedirectAttributes ra) {
        try {
            detalleFacturaService.eliminar(idDetalle);
            ra.addFlashAttribute("msgOk", "Detalle eliminado (ID " + idDetalle + ").");
        } catch (DataAccessException ex) {
            ra.addFlashAttribute("msgErr", humanizarErrorOracle(ex));
        }
        return redirectTo(idFactura);
    }


    @PostMapping("/{idFactura}/guardar")
    public String guardar(@PathVariable Long idFactura,
                          @RequestParam(required = false) Long id,
                          @RequestParam Long idArticulo,
                          @RequestParam Integer cantidad,
                          RedirectAttributes ra) {
        try {
            DetalleFactura d = new DetalleFactura();
            d.setId(id);
            d.setIdFactura(idFactura);
            d.setIdArticulo(idArticulo);
            d.setCantidad(cantidad);

            Long idResult = detalleFacturaService.guardar(d);
            ra.addFlashAttribute("msgOk",
                    (id == null ? "Detalle creado (ID " : "Detalle actualizado (ID ") + idResult + ").");
        } catch (DataAccessException ex) {
            ra.addFlashAttribute("msgErr", humanizarErrorOracle(ex));
        }
        return redirectTo(idFactura);
    }


    private String humanizarErrorOracle(DataAccessException ex) {
        Throwable cause = ex.getMostSpecificCause();
        String msg = cause != null ? cause.getMessage() : ex.getMessage();

        if (msg != null && msg.contains("ORA-20021")) {
            return "Stock insuficiente para el artículo seleccionado.";
        }
        if (msg != null && msg.contains("ORA-02292")) {
            return "No se puede eliminar: hay registros relacionados.";
        }
        if (msg != null) {
            int limit = Math.min(msg.length(), 300);
            return "Error: " + msg.substring(0, limit);
        }
        return "Ocurrió un error inesperado al procesar el detalle.";
    }
}
