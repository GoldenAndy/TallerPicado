package com.tallerpicado.controller;

import com.tallerpicado.domain.Articulo;
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

    @Autowired
    private FacturaService facturaService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private EmpleadoService empleadoService;
    // --- Nuevas inyecciones para DetalleFactura ---
    @Autowired
    private DetalleFacturaService detalleFacturaService;
    @Autowired
    private ArticuloService articuloService;

    // --- MÉTODOS DE FACTURA EXISTENTES (SIN CAMBIOS) ---
    @GetMapping("")
    public String listar(Model model) {
        model.addAttribute("facturas", facturaService.listarTodas());
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("empleados", empleadoService.obtenerTodos());
        return "facturas";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam("fechaEmision") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            @RequestParam("idCliente") Long idCliente,
            @RequestParam("idEmpleado") Long idEmpleado,
            @RequestParam("total") Double total) {
        Factura factura = new Factura();
        factura.setFechaEmision(fecha);
        factura.setTotal(total);
        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        factura.setCliente(cliente);
        Empleado empleado = new Empleado();
        empleado.setId(idEmpleado);
        factura.setEmpleado(empleado);
        facturaService.insertar(factura);
        return "redirect:/facturas";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
            @RequestParam("fechaEmision") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            @RequestParam("idCliente") Long idCliente,
            @RequestParam("idEmpleado") Long idEmpleado,
            @RequestParam("total") Double total) {
        Factura factura = new Factura();
        factura.setFechaEmision(fecha);
        factura.setTotal(total);
        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        factura.setCliente(cliente);
        Empleado empleado = new Empleado();
        empleado.setId(idEmpleado);
        factura.setEmpleado(empleado);
        facturaService.actualizar(id, factura);
        return "redirect:/facturas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return "redirect:/facturas";
    }

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

    // --- MÉTODOS AÑADIDOS PARA DETALLE DE FACTURA ---
    @GetMapping("/detalles/{idFactura}")
    public String verDetalles(@PathVariable("idFactura") Long idFactura, Model model) {
        Optional<Factura> facturaOpt = facturaService.obtenerPorId(idFactura);
        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();
            List<DetalleFactura> detalles = detalleFacturaService.listarPorFactura(idFactura);
            model.addAttribute("factura", factura);
            model.addAttribute("detalles", detalles);
            model.addAttribute("articulos", articuloService.obtenerTodos());
            return "detallefactura";
        }
        return "redirect:/facturas";
    }

    @PostMapping("/detalles/guardar")
    public String guardarDetalle(@RequestParam("idFactura") Long idFactura,
            @RequestParam("idArticulo") Long idArticulo,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("precioUnitario") Double precioUnitario) {
        DetalleFactura detalle = new DetalleFactura();
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        Factura factura = new Factura();
        factura.setId(idFactura);
        detalle.setIdFactura(idFactura);
        Articulo articulo = new Articulo();
        articulo.setId(idArticulo);
        detalle.setIdArticulo(idArticulo);
        detalleFacturaService.insertar(detalle);
        return "redirect:/facturas/detalles/" + idFactura;
    }

    @GetMapping("/detalles/eliminar/{idDetalle}")
    public String eliminarDetalle(@PathVariable("idDetalle") Long idDetalle,
            @RequestParam("idFactura") Long idFactura) {
        detalleFacturaService.eliminar(idDetalle);
        return "redirect:/facturas/detalles/" + idFactura;
    }
}
