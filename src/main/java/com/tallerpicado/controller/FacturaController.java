package com.tallerpicado.controller;

import com.tallerpicado.domain.Cliente;
import com.tallerpicado.domain.DetalleFactura;
import com.tallerpicado.domain.Empleado;
import com.tallerpicado.domain.Factura;
import com.tallerpicado.service.ArticuloService;
import com.tallerpicado.service.ClienteService;
import com.tallerpicado.service.DetalleFacturaService;
import com.tallerpicado.service.EmpleadoService;
import com.tallerpicado.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired private FacturaService facturaService;
    @Autowired private ClienteService clienteService;
    @Autowired private EmpleadoService empleadoService;

    // Para la pantalla de detalles
    @Autowired private DetalleFacturaService detalleFacturaService;
    @Autowired private ArticuloService articuloService;

    // ===== Listado =====
    @GetMapping("")
    public String listar(Model model) {
        model.addAttribute("facturas", facturaService.listarTodas());
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("empleados", empleadoService.obtenerTodos());
        return "facturas";
    }

    // ===== Crear (sin total) =====
    @PostMapping("/guardar")
    public String guardar(@RequestParam("fechaEmision") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
                          @RequestParam("idCliente") Long idCliente,
                          @RequestParam("idEmpleado") Long idEmpleado) {

        Factura factura = new Factura();
        factura.setFechaEmision(fecha);

        Cliente cliente = new Cliente(); cliente.setId(idCliente);
        Empleado empleado = new Empleado(); empleado.setId(idEmpleado);
        factura.setCliente(cliente);
        factura.setEmpleado(empleado);

        Long nuevoId = facturaService.insertar(factura);
        return "redirect:/facturas/detalles/" + nuevoId;
    }

    // ===== Actualizar (sin total) =====
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam("fechaEmision") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
                             @RequestParam("idCliente") Long idCliente,
                             @RequestParam("idEmpleado") Long idEmpleado) {

        Factura factura = new Factura();
        factura.setFechaEmision(fecha);

        Cliente cliente = new Cliente(); cliente.setId(idCliente);
        Empleado empleado = new Empleado(); empleado.setId(idEmpleado);
        factura.setCliente(cliente);
        factura.setEmpleado(empleado);

        facturaService.actualizar(id, factura);
        return "redirect:/facturas";
    }

    // ===== Eliminar =====
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return "redirect:/facturas";
    }

    // ===== Buscar =====
    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String cliente,
                         @RequestParam(required = false) String idFactura,
                         Model model) {
        List<Factura> resultados;
        if (cliente != null && !cliente.isBlank()) {
            resultados = facturaService.buscarPorCliente(cliente);
        } else if (idFactura != null && !idFactura.isBlank()) {
            resultados = facturaService.buscarPorId(idFactura);
        } else {
            resultados = facturaService.listarTodas();
        }
        model.addAttribute("facturas", resultados);
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("empleados", empleadoService.obtenerTodos());
        return "facturas";
    }

    // ===== Detalles de factura (ruta: /facturas/detalles/{idFactura}) =====
    @GetMapping("/detalles/{idFactura}")
    public String verDetalles(@PathVariable Long idFactura, Model model) {
        Optional<Factura> facturaOpt = facturaService.obtenerPorId(idFactura);
        if (facturaOpt.isEmpty()) {
            return "redirect:/facturas";
        }
        Factura factura = facturaOpt.get();
        List<DetalleFactura> detalles = detalleFacturaService.listarPorFactura(idFactura);

        model.addAttribute("factura", factura);
        model.addAttribute("detalles", detalles);
        model.addAttribute("articulos", articuloService.obtenerTodos());
        model.addAttribute("idFactura", idFactura); // útil para forms en la vista

        return "detallefactura";
    }

    // ===== Crear detalle (sin precioUnitario) =====
    @PostMapping("/detalles/guardar")
    public String guardarDetalle(@RequestParam("idFactura") Long idFactura,
                                 @RequestParam("idArticulo") Long idArticulo,
                                 @RequestParam("cantidad") Integer cantidad) {

        DetalleFactura d = new DetalleFactura();
        d.setIdFactura(idFactura);
        d.setIdArticulo(idArticulo);
        d.setCantidad(cantidad);
        // NO seteamos precioUnitario: lo define el package desde ARTICULOS

        detalleFacturaService.insertar(d);
        return "redirect:/facturas/detalles/" + idFactura;
    }

    // ===== Eliminar detalle =====
    @GetMapping("/detalles/eliminar/{idDetalle}")
    public String eliminarDetalle(@PathVariable("idDetalle") Long idDetalle,
                                  @RequestParam("idFactura") Long idFactura) {
        detalleFacturaService.eliminar(idDetalle);
        return "redirect:/facturas/detalles/" + idFactura;
    }
}
