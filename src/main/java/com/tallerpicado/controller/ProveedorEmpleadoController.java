package com.tallerpicado.controller;

import com.tallerpicado.domain.ProveedorEmpleado;
import com.tallerpicado.service.ProveedorEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedor-empleado")
@CrossOrigin(origins = "*")
public class ProveedorEmpleadoController {

    @Autowired
    private ProveedorEmpleadoService service;

    @GetMapping
    public List<ProveedorEmpleado> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public ProveedorEmpleado asignar(@RequestBody ProveedorEmpleado relacion) {
        return service.asignarEmpleado(relacion);
    }

    @DeleteMapping("/{idProveedor}/{idEmpleado}")
    public void eliminar(@PathVariable Long idProveedor, @PathVariable Long idEmpleado) {
        service.eliminarRelacion(idProveedor, idEmpleado);
    }
}
