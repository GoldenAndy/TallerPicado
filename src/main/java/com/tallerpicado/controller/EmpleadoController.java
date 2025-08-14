package com.tallerpicado.controller;

import com.tallerpicado.domain.Empleado;
import com.tallerpicado.service.EmpleadoService;
import com.tallerpicado.service.PuestoService;
import com.tallerpicado.service.ProveedorService;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PuestoService puestoService;

    @Autowired
    private ProveedorService proveedorService;

 
    @GetMapping
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.obtenerTodos());
        model.addAttribute("puestos", puestoService.listarPuestos());
        model.addAttribute("proveedores", proveedorService.obtenerTodos());
        return "empleados";
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@ModelAttribute Empleado empleado,
                                   @RequestParam(value = "proveedores", required = false) List<Long> proveedores) {
        empleado.setProveedores(proveedores);
        empleadoService.guardar(empleado);
        return "redirect:/empleados";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarEmpleado(@PathVariable Long id,
                                     @ModelAttribute Empleado empleado,
                                     @RequestParam(value = "proveedores", required = false) List<Long> proveedores) {
        empleado.setProveedores(proveedores);
        empleadoService.actualizar(id, empleado);
        return "redirect:/empleados";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            empleadoService.eliminar(id);
            redirect.addFlashAttribute("success", "Empleado eliminado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            if (msg.contains("ORA-20071")) {
                redirect.addFlashAttribute("error", msg);
            } else if (msg.contains("ORA-20073")) { 
                redirect.addFlashAttribute("error", "El empleado indicado no existe.");
            } else if (msg.contains("ORA-02292")) { 
                redirect.addFlashAttribute("error", "No es posible eliminar: el empleado tiene registros relacionados.");
            } else {
                redirect.addFlashAttribute("error", "No se pudo eliminar el empleado.");
            }
        }
        return "redirect:/empleados";
    }


    @GetMapping("/buscar")
    public String buscarPorNombre(@RequestParam("nombre") String nombre, Model model) {
        model.addAttribute("empleados", empleadoService.buscarPorNombre(nombre));
        model.addAttribute("puestos", puestoService.listarPuestos());
        model.addAttribute("proveedores", proveedorService.obtenerTodos());
        return "empleados";
    }

    @GetMapping("/filtrar/puesto")
    public String filtrarPorPuesto(@RequestParam("id") Long idPuesto, Model model) {
        model.addAttribute("empleados", empleadoService.filtrarPorPuesto(idPuesto));
        model.addAttribute("puestos", puestoService.listarPuestos());
        model.addAttribute("proveedores", proveedorService.obtenerTodos());
        return "empleados";
    }

    @GetMapping("/filtrar/proveedor")
    public String filtrarPorProveedor(@RequestParam("id") Long idProveedor, Model model) {
        model.addAttribute("empleados", empleadoService.filtrarPorProveedor(idProveedor));
        model.addAttribute("puestos", puestoService.listarPuestos());
        model.addAttribute("proveedores", proveedorService.obtenerTodos());
        return "empleados";
    }

    @GetMapping("/filtrar/estado")
    public String filtrarPorEstado(@RequestParam("estado") String estado, Model model) {
        model.addAttribute("empleados", empleadoService.filtrarPorEstado(estado));
        model.addAttribute("puestos", puestoService.listarPuestos());
        model.addAttribute("proveedores", proveedorService.obtenerTodos());
        return "empleados";
    }
    
    
    @GetMapping("/obtener/{id}")
    @ResponseBody
    public Empleado obtenerEmpleadoPorId(@PathVariable Long id) {
        return empleadoService.obtenerPorId(id).orElse(null);
    }
    
    @GetMapping("/filtros")
    public String filtrarEmpleados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long idPuesto,
            @RequestParam(required = false) Long idProveedor,
            @RequestParam(required = false) String estado,
            Model model) {

        List<Empleado> empleadosFiltrados = empleadoService.filtrarMultiples(nombre, idPuesto, idProveedor, estado);

        model.addAttribute("empleados", empleadosFiltrados);
        model.addAttribute("puestos", puestoService.listarPuestos());
        model.addAttribute("proveedores", proveedorService.obtenerTodos());

        return "empleados";
    }
    

    @GetMapping("/autocompletar")
    @ResponseBody
    public List<Map<String, Object>> autocompletarEmpleados(@RequestParam("q") String query) {
        List<Empleado> coincidencias = empleadoService.buscarPorNombre(query);

        List<Map<String, Object>> resultados = new ArrayList<>();
        for (Empleado e : coincidencias) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", e.getId());
            map.put("nombre", e.getNombre());
            resultados.add(map);
        }
        return resultados;
    }


}

