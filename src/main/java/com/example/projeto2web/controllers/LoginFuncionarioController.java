package com.example.projeto2web.controllers;

import com.example.projeto2.models.Funcionario;
import com.example.projeto2.services.FuncionarioService;
import com.example.projeto2web.utils.SessaoUtilizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/funcionario")
public class LoginFuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public String showLoginPage(@RequestParam(value = "erro", required = false) String erro, Model model) {
        model.addAttribute("erro", erro != null);
        return "login-funcionario";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        Funcionario f = funcionarioService.login(username, password).orElse(null);

        if (f != null) {
            SessaoUtilizador.setFuncionario(session, f);

            if (f.getIdtipofunc().getId() == 1) {
                return "redirect:/gerente/home";
            } else {
                return "redirect:/funcionario2/home";
            }
        }

        return "redirect:/funcionario?erro=true";
    }
}
