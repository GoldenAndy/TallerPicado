package com.tallerpicado.controller;

import com.tallerpicado.domain.Cliente;
import com.tallerpicado.service.ClienteService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Mostrar lista de clientes
    @GetMapping
    public String mostrarClientes(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "clientes"; // Thymeleaf buscará clientes.html
    }

    // Guardar nuevo cliente (desde el modal)
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    // Eliminar cliente
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }
    
    
    
    @GetMapping("/buscar")
    public String buscarClientes(@RequestParam("nombre") String nombre, Model model) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        if (clientes == null) {
            clientes = new ArrayList<>();
        }
        model.addAttribute("clientes", clientes);
        model.addAttribute("usuario", "TALLERPICADO"); // si usás esto en el header
        return "clientes";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(@PathVariable Long id, @ModelAttribute Cliente cliente) {
        clienteService.actualizar(id, cliente);
        return "redirect:/clientes";
    }


}
