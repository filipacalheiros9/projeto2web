package com.example.projeto2web.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/irParaFuncionario")
    public String redirecionarFuncionario() {
        return "redirect:/funcionario";
    }

    @GetMapping("/funcionario/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
