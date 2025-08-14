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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String guardarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirect) {
        try {
            clienteService.guardar(cliente);
            redirect.addFlashAttribute("success", "Cliente registrado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String m = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            String mensaje;
            if (m.contains("ORA-20088") || m.contains("ORA-20085")) {
                mensaje = m;
            } else if (m.contains("ORA-00001")) {
                if (m.contains("UX_CLI_CEDULA_NORM"))       mensaje = "La cédula ya está registrada.";
                else if (m.contains("UX_CLI_CELULAR_NORM")) mensaje = "El celular ya está registrado.";
                else if (m.contains("UX_CLI_CORREO_NORM"))  mensaje = "El correo ya está registrado.";
                else mensaje = "La cédula, celular o correo ya están registrados.";
            } else {
                mensaje = "No se pudo registrar el cliente.";
            }
            redirect.addFlashAttribute("errorNuevoCli", mensaje);
            redirect.addFlashAttribute("openNuevoCli", true);
            redirect.addFlashAttribute("cliNombreIntento",  cliente.getNombre());
            redirect.addFlashAttribute("cliCedulaIntento",  cliente.getCedula());
            redirect.addFlashAttribute("cliCelularIntento", cliente.getCelular());
            redirect.addFlashAttribute("cliCorreoIntento",  cliente.getCorreo());
        }
        return "redirect:/clientes";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            clienteService.eliminar(id); 
            redirect.addFlashAttribute("success", "Cliente eliminado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            if (msg.contains("ORA-20081")) {  
                redirect.addFlashAttribute("error", msg);
            } else if (msg.contains("ORA-20083")) {
                redirect.addFlashAttribute("error", "El cliente indicado no existe.");
            } else if (msg.contains("ORA-02292")) {
                redirect.addFlashAttribute("error", "No es posible eliminar: el cliente tiene registros relacionados.");
            } else {
                redirect.addFlashAttribute("error", "No se pudo eliminar el cliente.");
            }
        }
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
    public String actualizarCliente(@PathVariable Long id,
                                    @ModelAttribute Cliente cliente,
                                    RedirectAttributes redirect) {
        try {
            clienteService.actualizar(id, cliente);
            redirect.addFlashAttribute("success", "Cliente actualizado correctamente.");
        } catch (org.springframework.dao.DataAccessException e) {
            String m = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
            String mensaje;
            if (m.contains("ORA-20086") || m.contains("ORA-20088")) {
                mensaje = m;
            } else if (m.contains("ORA-20087")) { 
                redirect.addFlashAttribute("error", "El cliente indicado no existe.");
                return "redirect:/clientes";
            } else if (m.contains("ORA-00001")) {
                if (m.contains("UX_CLI_CEDULA_NORM"))       mensaje = "La cédula ya está registrada.";
                else if (m.contains("UX_CLI_CELULAR_NORM")) mensaje = "El celular ya está registrado.";
                else if (m.contains("UX_CLI_CORREO_NORM"))  mensaje = "El correo ya está registrado.";
                else mensaje = "La cédula, celular o correo ya están registrados.";
            } else {
                mensaje = "No se pudo actualizar el cliente.";
            }
            redirect.addFlashAttribute("errorEditarCli", mensaje);
            redirect.addFlashAttribute("openEditarCli", true);
            // Rehidratar campos del modal "Editar"
            redirect.addFlashAttribute("editIdCli",      id);
            redirect.addFlashAttribute("editNombreCli",  cliente.getNombre());
            redirect.addFlashAttribute("editCedulaCli",  cliente.getCedula());
            redirect.addFlashAttribute("editCelularCli", cliente.getCelular());
            redirect.addFlashAttribute("editCorreoCli",  cliente.getCorreo());
        }
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
