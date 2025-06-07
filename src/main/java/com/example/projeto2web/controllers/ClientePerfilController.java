package com.example.projeto2web.controllers;

import com.example.projeto2.models.Cliente;
import com.example.projeto2.services.ClienteService;
import com.example.projeto2.services.CodPostalClienteService;
import com.example.projeto2web.utils.SessaoUtilizador;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ClientePerfilController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CodPostalClienteService codPostalClienteService;

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) {
            return "redirect:/cliente/login";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("codigosPostais", codPostalClienteService.listarTodos());
        return "cliente-perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@ModelAttribute Cliente clienteAtualizado, HttpSession session, Model model) {
        Cliente clienteOriginal = SessaoUtilizador.getCliente(session);
        if (clienteOriginal == null) {
            return "redirect:/cliente/login";
        }

        try {
            clienteAtualizado.setId(clienteOriginal.getId());
            clienteAtualizado.setUsername(clienteOriginal.getUsername());
            clienteAtualizado.setPass(clienteOriginal.getPass()); // Senha não alterável aqui

            clienteService.atualizar(clienteAtualizado.getId(), clienteAtualizado);
            SessaoUtilizador.setCliente(session, clienteAtualizado);
            model.addAttribute("sucesso", "Dados atualizados com sucesso!");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao atualizar perfil: " + e.getMessage());
        }

        model.addAttribute("cliente", clienteAtualizado);
        model.addAttribute("codigosPostais", codPostalClienteService.listarTodos());
        return "cliente-perfil";
    }

    @PostMapping("/eliminar")
    public String eliminarConta(HttpSession session) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente != null) {
            clienteService.excluir(cliente.getId());
            session.invalidate();
        }
        return "redirect:/";
    }
}
