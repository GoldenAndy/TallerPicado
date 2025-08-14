package com.tallerpicado.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String mostrarInicio(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuarioConectado");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuarioConectado", usuario);
        return "index";
    }
}
