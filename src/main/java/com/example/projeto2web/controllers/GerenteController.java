package com.example.projeto2web.controllers;

import com.example.projeto2.models.Funcionario;
import com.example.projeto2.services.FuncionarioService;
import com.example.projeto2web.utils.SessaoUtilizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/gerente")
public class GerenteController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/home")
    public String home() {
        return "gerente";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Funcionario gerente = SessaoUtilizador.getFuncionario(session);
        if (gerente == null || gerente.getIdtipofunc().getId() != 1) {
            return "redirect:/funcionario";
        }

        model.addAttribute("funcionario", gerente);
        model.addAttribute("cargos", List.of("Gerente", "Funcionário"));
        return "gerente-perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@ModelAttribute Funcionario funcionario, HttpSession session, Model model) {
        Funcionario gerente = SessaoUtilizador.getFuncionario(session);
        if (gerente == null || gerente.getIdtipofunc().getId() != 1) {
            return "redirect:/funcionario";
        }

        try {
            funcionario.setId(gerente.getId());
            Funcionario original = funcionarioService.procurarPorId(funcionario.getId())
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
            funcionario.setPassword(original.getPassword());
            funcionarioService.atualizar(funcionario.getId(), funcionario);
            SessaoUtilizador.setFuncionario(session, funcionario);
            model.addAttribute("sucesso", "Perfil atualizado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao atualizar perfil: " + e.getMessage());
        }

        model.addAttribute("funcionario", funcionario);
        model.addAttribute("cargos", List.of("Gerente", "Funcionário"));
        return "gerente-perfil";
    }

    @GetMapping("/funcionarios")
    public String funcionarios() {
        return "gerente-funcionario";
    }

    @GetMapping("/stock")
    public String stock() {
        return "gerente-stock";
    }

    @GetMapping("/fornecedor")
    public String fornecedor() {
        return "gerente-fornecedor";
    }

    @GetMapping("/projetos")
    public String projetos() {
        return "gerente-projetos";
    }

}
