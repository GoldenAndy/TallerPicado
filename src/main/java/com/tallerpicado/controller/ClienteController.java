package com.tallerpicado.controller;

import com.tallerpicado.domain.Cliente;
import com.tallerpicado.service.ClienteService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    @GetMapping
    public String mostrarClientes(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "clientes";
    }


    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }


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
        model.addAttribute("usuario", "TALLERPICADO");
        return "clientes";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(@PathVariable Long id, @ModelAttribute Cliente cliente) {
        clienteService.actualizar(id, cliente);
        return "redirect:/clientes";
    }

@GetMapping("/autocompletar")
@ResponseBody
public List<Map<String, Object>> autocompletarClientes(@RequestParam("q") String query) {
    List<Cliente> coincidencias = clienteService.buscarPorNombre(query);

    List<Map<String, Object>> resultados = new ArrayList<>();
    for (Cliente c : coincidencias) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", c.getId());
        map.put("nombre", c.getNombre());
        resultados.add(map);
    }
    return resultados;
}


}
