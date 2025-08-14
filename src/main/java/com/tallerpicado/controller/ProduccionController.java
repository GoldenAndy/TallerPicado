package com.tallerpicado.controller;

import com.tallerpicado.domain.Maquinaria;
import com.tallerpicado.domain.OrdenTrabajo;
import com.tallerpicado.domain.Produccion;
import com.tallerpicado.service.MaquinariaService;
import com.tallerpicado.service.OrdenTrabajoService;
import com.tallerpicado.service.ProduccionService;
import org.springframework.format.annotation.DateTimeFormat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/produccion")
public class ProduccionController {

    @Autowired
    private ProduccionService produccionService;

    @Autowired
    private MaquinariaService maquinariaService;

    @Autowired
    private OrdenTrabajoService OrdenTrabajoService;


    @GetMapping("/orden/{idOrden}")
    public String verProduccionPorOrden(@PathVariable Long idOrden, Model model) {
        List<Produccion> producciones = produccionService.buscarPorOrden(idOrden);
        List<Maquinaria> maquinasDisponibles = maquinariaService.obtenerPorEstado("DISPONIBLE");

        model.addAttribute("idOrden", idOrden);
        model.addAttribute("producciones", producciones);
        model.addAttribute("maquinas", maquinasDisponibles);

        return "produccion";
    }


    @PostMapping("/guardar")
    public String guardarProduccion(@RequestParam Long idMaquina,
                                    @RequestParam Long idOrden) {
        produccionService.insertarProduccion(idMaquina, idOrden);
        return "redirect:/produccion/orden/" + idOrden;
    }


    @PostMapping("/actualizar/{id}")
    public String actualizarProduccion(@PathVariable Long id,
                                       @RequestParam Long idMaquina,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
                                       @RequestParam String estado,
                                       @RequestParam Long idOrden) {
        Produccion p = new Produccion();

        p.setId(id);

        Maquinaria m = new Maquinaria();
        m.setId(idMaquina);
        p.setMaquina(m);

        OrdenTrabajo o = new OrdenTrabajo();
        o.setId(idOrden);
        p.setOrden(o);

        p.setFechaInicio(fechaInicio);
        p.setFechaFin(fechaFin);
        p.setEstado(estado);

        produccionService.actualizarProduccion(p);
        return "redirect:/produccion/orden/" + idOrden;
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarProduccion(@PathVariable Long id) {
        List<Produccion> todas = produccionService.listarTodas();
        Long idOrden = todas.stream()
                            .filter(p -> p.getId().equals(id))
                            .map(p -> p.getOrden().getId())
                            .findFirst()
                            .orElse(0L);

        produccionService.eliminarProduccion(id);
        return "redirect:/produccion/orden/" + idOrden;
    }
}

