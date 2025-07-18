package com.tallerpicado.controller;

import com.tallerpicado.domain.Proveedor;
import com.tallerpicado.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // Mostrar vista principal con todos los proveedores
    @GetMapping
    public String listarProveedores(Model model) {
        List<Proveedor> proveedores = proveedorService.obtenerTodos();
        model.addAttribute("proveedores", proveedores);
        return "proveedores";
    }

    // Guardar un nuevo proveedor desde el formulario
    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor) {
        proveedorService.guardar(proveedor);
        return "redirect:/proveedores";
    }

    // Actualizar proveedor existente desde el modal
    @PostMapping("/actualizar/{id}")
    public String actualizarProveedor(@PathVariable Long id, @ModelAttribute Proveedor proveedor) {
        proveedorService.actualizar(id, proveedor);
        return "redirect:/proveedores";
    }

    // Eliminar proveedor
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminar(id);
        return "redirect:/proveedores";
    }

    // Buscar proveedores por nombre (regex)
    @GetMapping("/buscar")
    public String buscarProveedores(@RequestParam("nombre") String nombre, Model model) {
        List<Proveedor> resultados = proveedorService.buscarPorNombreRegex(nombre);
        model.addAttribute("proveedores", resultados);
        return "proveedores";
    }
}
