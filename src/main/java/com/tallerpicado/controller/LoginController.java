package com.tallerpicado.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Controller
public class LoginController {

    // Muestra la página de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Procesa el intento de login
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String usuario,
                                 @RequestParam String contrasena,
                                 HttpSession session,
                                 Model model) {

        // IP de la máquina virtual y el servicio ORCLPDB
        String url = "jdbc:oracle:thin:@192.168.100.69:1521/ORCLPDB";

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            // Si conecta, se guarda en sesión
            session.setAttribute("usuarioConectado", usuario);
            return "redirect:/"; // Redirige al index u home
        } catch (SQLException e) {
            model.addAttribute("error", "Usuario o contraseña inválidos.");
            return "login";
        }
    }

    // Cierra la sesión actual
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
