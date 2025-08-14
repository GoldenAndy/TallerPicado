package com.tallerpicado.controller;

import com.tallerpicado.domain.Cliente;
import com.tallerpicado.domain.Empleado;
import com.tallerpicado.domain.OrdenTrabajo;
import com.tallerpicado.service.ClienteService;
import com.tallerpicado.service.EmpleadoService;
import com.tallerpicado.service.OrdenTrabajoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ordenes")
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String mostrarOrdenes(Model model) {
        List<OrdenTrabajo> ordenes = ordenTrabajoService.obtenerTodas();
        List<String> estados = Arrays.asList("PENDIENTE", "EN_PROCESO", "FINALIZADA", "ENTREGADA");

        model.addAttribute("ordenes", ordenes);
        model.addAttribute("estados", estados);

        return "ordenes";
    }

@PostMapping("/guardar")
public String guardarOrden(@RequestParam("clienteNombre") String clienteNombre,
                           @RequestParam("empleadoNombre") String empleadoNombre,
                           @RequestParam("estado") String estado) {

    System.out.println("=== [DEBUG - GUARDAR ORDEN] ===");
    System.out.println("Cliente ingresado: " + clienteNombre);
    System.out.println("Empleado ingresado: " + empleadoNombre);
    System.out.println("Estado seleccionado: " + estado);

    Optional<Cliente> clienteOpt = clienteService.buscarPorNombreExacto(clienteNombre);
    Optional<Empleado> empleadoOpt = empleadoService.buscarPorNombreExacto(empleadoNombre);

    if (clienteOpt.isEmpty() && empleadoOpt.isEmpty()) {
        System.out.println("❌ ERROR: Cliente y Empleado NO encontrados.");
        throw new RuntimeException("Cliente y Empleado no encontrados");
    } else if (clienteOpt.isEmpty()) {
        System.out.println("❌ ERROR: Cliente NO encontrado.");
        throw new RuntimeException("Cliente no encontrado");
    } else if (empleadoOpt.isEmpty()) {
        System.out.println("❌ ERROR: Empleado NO encontrado.");
        throw new RuntimeException("Empleado no encontrado");
    }

    System.out.println("✅ Ambos encontrados correctamente");

    OrdenTrabajo orden = new OrdenTrabajo();
    orden.setCliente(clienteOpt.get());
    orden.setEmpleado(empleadoOpt.get());
    orden.setEstado(estado);
    orden.setFecha(new java.util.Date());

    ordenTrabajoService.guardar(orden);
    System.out.println("✅ Orden guardada correctamente.");
    return "redirect:/ordenes";
}


@PostMapping("/actualizar/{id}")
public String actualizarOrden(@PathVariable Long id,
                              @RequestParam("clienteNombre") String clienteNombre,
                              @RequestParam("empleadoNombre") String empleadoNombre,
                              @RequestParam("estado") String estado) {

    System.out.println("=== [DEBUG - ACTUALIZAR ORDEN] ===");
    System.out.println("Cliente ingresado: " + clienteNombre);
    System.out.println("Empleado ingresado: " + empleadoNombre);
    System.out.println("Estado seleccionado: " + estado);

    Optional<Cliente> clienteOpt = clienteService.buscarPorNombreExacto(clienteNombre);
    Optional<Empleado> empleadoOpt = empleadoService.buscarPorNombreExacto(empleadoNombre);

    if (clienteOpt.isEmpty() || empleadoOpt.isEmpty()) {
        System.out.println("❌ Error: Cliente o Empleado no encontrados");
        throw new RuntimeException("Cliente o Empleado no encontrados");
    }

    OrdenTrabajo orden = new OrdenTrabajo();
    orden.setId(id);
    orden.setCliente(clienteOpt.get());
    orden.setEmpleado(empleadoOpt.get());
    orden.setEstado(estado);
    orden.setFecha(new Date());

    ordenTrabajoService.actualizar(id, orden);
    System.out.println("✅ Orden actualizada correctamente.");
    return "redirect:/ordenes";
}

    
    @GetMapping("/eliminar/{id}")
    public String eliminarOrden(
            @PathVariable Long id,
            @RequestParam(name = "confirmar", defaultValue = "0") int confirmar,
            RedirectAttributes ra) {
        try {

            ordenTrabajoService.eliminar(id, confirmar);


            if (confirmar == 1) {
                ra.addFlashAttribute("ok", "Orden eliminada junto con sus producciones.");
            } else {
                ra.addFlashAttribute("ok", "Orden eliminada.");
            }
            return "redirect:/ordenes";

        } catch (org.springframework.dao.DataAccessException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                int code = sqlEx.getErrorCode();

                if (code == 20050) {
                    ra.addFlashAttribute("requiereConfirmacion", true);
                    ra.addFlashAttribute("idPendiente", id);
                    ra.addFlashAttribute("msg",
                            "La orden actual tiene producciones en curso, ¿desea borrar ambas?");
                    return "redirect:/ordenes";
                }

                if (code == 20051) {
                    ra.addFlashAttribute("error", "La orden no existe.");
                    return "redirect:/ordenes";
                }
            }
            ra.addFlashAttribute("error", "No se pudo eliminar la orden: " + e.getMessage());
            return "redirect:/ordenes";
        }
    }

    @GetMapping("/ver/{id}")
    public String verOrden(@PathVariable Long id, Model model) {
        OrdenTrabajo orden = ordenTrabajoService.obtenerPorId(id).orElse(null);
        model.addAttribute("orden", orden);
        return "detalle_orden";
    }


    @GetMapping("/clientes/autocompletar")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> autocompletarClientes(@RequestParam("q") String query) {
        List<Cliente> resultados = clienteService.buscarPorNombre(query);
        List<Map<String, Object>> respuesta = resultados.stream().map(cliente -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cliente.getId());
            map.put("nombre", cliente.getNombre());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }


    @GetMapping("/empleados/autocompletar")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> autocompletarEmpleados(@RequestParam("q") String query) {
        List<Empleado> resultados = empleadoService.buscarPorNombre(query);
        List<Map<String, Object>> respuesta = resultados.stream().map(empleado -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", empleado.getId());
            map.put("nombre", empleado.getNombre() + " " + empleado.getApellido());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }
    
    
    @GetMapping("/filtros")
    public String filtrarOrdenes(@RequestParam(required = false) String cliente,
                                 @RequestParam(required = false) String estado,
                                 Model model) {

        List<OrdenTrabajo> ordenesFiltradas;

        boolean clientePresente = cliente != null && !cliente.isBlank();
        boolean estadoPresente = estado != null && !estado.isBlank();

        if (clientePresente && estadoPresente) {
            ordenesFiltradas = ordenTrabajoService.buscarPorClienteYEstado(cliente, estado);
        } else if (clientePresente) {
            ordenesFiltradas = ordenTrabajoService.buscarPorNombreCliente(cliente);
        } else if (estadoPresente) {
            ordenesFiltradas = ordenTrabajoService.buscarPorEstado(estado);
        } else {
            ordenesFiltradas = ordenTrabajoService.obtenerTodas(); 
        }

        model.addAttribute("ordenes", ordenesFiltradas);
        model.addAttribute("estados", Arrays.asList("PENDIENTE", "EN_PROCESO", "FINALIZADA", "ENTREGADA"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("estado", estado);

        return "ordenes";
    }

}
