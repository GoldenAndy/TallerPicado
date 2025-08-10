package com.tallerpicado.controller;
 
import com.tallerpicado.domain.DetalleFactura;
import com.tallerpicado.service.DetalleFacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
 
@Controller
@RequestMapping("/facturas/{idFactura}/detalles")
@RequiredArgsConstructor
public class DetalleFacturaController {
 
    private final DetalleFacturaService detalleFacturaService;
 
    @GetMapping
    public String listar(
            @PathVariable Long idFactura,
            @RequestParam(required = false) String patron,
            Model model
    ) {
        List<DetalleFactura> detalles = (patron == null || patron.isBlank())
                ? detalleFacturaService.listarPorFactura(idFactura)
                : detalleFacturaService.buscarPorArticulo(idFactura, patron);
 
        model.addAttribute("detalles", detalles);
        model.addAttribute("idFactura", idFactura);
        model.addAttribute("patron", patron);
        return "facturas/detalles"; // <- tu vista Thymeleaf (ajústalo al nombre real)
    }
 
    @PostMapping("/crear")
    public String crear(
            @PathVariable Long idFactura,
            @RequestParam Long idArticulo,
            @RequestParam Integer cantidad,
            @RequestParam Double precioUnitario,
            RedirectAttributes ra
    ) {
        DetalleFactura d = new DetalleFactura();
        d.setIdFactura(idFactura);
        d.setIdArticulo(idArticulo);
        d.setCantidad(cantidad);
        d.setPrecioUnitario(precioUnitario);
 
        Long nuevoId = detalleFacturaService.insertar(d);
        ra.addFlashAttribute("msgOk", "Detalle creado (ID " + nuevoId + ").");
        return "redirect:/facturas/" + "detalles/" + idFactura;
    }
 
    @PostMapping("/{idDetalle}/actualizar")
    public String actualizar(
            @PathVariable Long idFactura,
            @PathVariable Long idDetalle,
            @RequestParam Long idArticulo,
            @RequestParam Integer cantidad,
            @RequestParam Double precioUnitario,
            RedirectAttributes ra
    ) {
        DetalleFactura d = new DetalleFactura();
        d.setId(idDetalle);
        d.setIdArticulo(idArticulo);
        d.setCantidad(cantidad);
        d.setPrecioUnitario(precioUnitario);
 
        detalleFacturaService.actualizar(d);
        ra.addFlashAttribute("msgOk", "Detalle actualizado (ID " + idDetalle + ").");
        return "redirect:/facturas/" + "detalles/" + idFactura;
    }
 

    @PostMapping("/{idDetalle}/eliminar")
    public String eliminar(
            @PathVariable Long idFactura,
            @PathVariable Long idDetalle,
            RedirectAttributes ra
    ) {
        detalleFacturaService.eliminar(idDetalle);
        ra.addFlashAttribute("msgOk", "Detalle eliminado (ID " + idDetalle + ").");
        return "redirect:/facturas/" + "detalles/" + idFactura;
    }
 

    @PostMapping("/guardar")
    public String guardar(
            @PathVariable Long idFactura,
            @RequestParam(required = false) Long id,
            @RequestParam Long idArticulo,
            @RequestParam Integer cantidad,
            @RequestParam Double precioUnitario,
            RedirectAttributes ra
    ) {
        DetalleFactura d = new DetalleFactura();
        d.setId(id);
        d.setIdFactura(idFactura);
        d.setIdArticulo(idArticulo);
        d.setCantidad(cantidad);
        d.setPrecioUnitario(precioUnitario);
 
        Long idResult = detalleFacturaService.guardar(d);
        ra.addFlashAttribute("msgOk", (id == null ? "Detalle creado (ID " : "Detalle actualizado (ID ") + idResult + ").");
        return "redirect:/facturas/" + "detalles/" + idFactura;
    }

    @GetMapping("/buscar")
    public String buscar(
            @PathVariable Long idFactura,
            @RequestParam String patron,
            Model model
    ) {
        List<DetalleFactura> detalles = detalleFacturaService.buscarPorArticulo(idFactura, patron);
        model.addAttribute("detalles", detalles);
        model.addAttribute("idFactura", idFactura);
        model.addAttribute("patron", patron);
        return "facturas/detalles"; // misma vista de listado
    }
}