package com.example.projeto2web.controllers;

import com.example.projeto2web.utils.SessaoUtilizador;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @GetMapping("/home")
    public String home(HttpSession session) {
        if (SessaoUtilizador.getCliente(session) == null) {
            return "redirect:/cliente/login";
        }
        return "cliente";
    }

    @GetMapping("/cliente/projetos")
    public String verProjetos(HttpSession session) {
        if (SessaoUtilizador.getCliente(session) == null) {
            return "redirect:/cliente/login";
        }
        return "cliente-projetos";
    }
}
