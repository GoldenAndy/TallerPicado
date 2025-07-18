package com.tallerpicado.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrdenTrabajoController {

    @GetMapping("/ordenes")
    public String mostrarVistaOrdenes(Model model) {
        return "ordenes";
    }
}
