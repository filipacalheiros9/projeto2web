package com.example.projeto2web.controllers;

import com.example.projeto2.models.Funcionario;
import com.example.projeto2web.utils.SessaoUtilizador;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/funcionario2")
public class Funcionario2Controller {

    @GetMapping("/home")
    public String homeFuncionario(HttpSession session, Model model) {
        Funcionario funcionario = SessaoUtilizador.getFuncionario(session);
        if (funcionario == null || funcionario.getIdtipofunc().getId() != 2) {
            return "redirect:/funcionario";
        }

        model.addAttribute("funcionario", funcionario);
        return "funcionario";
    }

    @GetMapping("/stock")
    public String gestao() {
        return "funcionario-stock";
    }

    @GetMapping("/cliente")
    public String cliente() {
        return "funcionario-cliente";
    }
}
