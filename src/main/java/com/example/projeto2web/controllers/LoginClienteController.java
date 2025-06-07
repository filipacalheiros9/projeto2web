package com.example.projeto2web.controllers;

import com.example.projeto2.models.Cliente;
import com.example.projeto2.models.CodPostalCliente;
import com.example.projeto2.services.ClienteService;
import com.example.projeto2.services.CodPostalClienteService;
import com.example.projeto2web.utils.SessaoUtilizador;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class LoginClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CodPostalClienteService codPostalClienteService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login-cliente";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 Model model, HttpSession session) {
        Optional<Cliente> clienteOpt = clienteService.listarTodos().stream()
                .filter(c -> c.getUsername().equals(username) && c.getPass().equals(password))
                .findFirst();

        if (clienteOpt.isPresent()) {
            SessaoUtilizador.setCliente(session, clienteOpt.get());
            return "redirect:/cliente/home";
        } else {
            model.addAttribute("erro", true);
            return "login-cliente";
        }
    }

    @GetMapping("/registar")
    public String mostrarFormularioRegisto(Model model) {
        model.addAttribute("codigosPostais", codPostalClienteService.listarTodos());
        return "registar-cliente";
    }

    @PostMapping("/registar")
    public String processarFormularioRegisto(@RequestParam String nomecliente,
                                             @RequestParam String telefonecliente,
                                             @RequestParam String ruacliente,
                                             @RequestParam Integer nportacliente,
                                             @RequestParam String nifcliente,
                                             @RequestParam String codpostalcli,
                                             @RequestParam String username,
                                             @RequestParam String pass,
                                             Model model) {
        try {
            Optional<CodPostalCliente> codPostal = codPostalClienteService.procurarPorCodigo(codpostalcli);
            if (codPostal.isEmpty()) {
                model.addAttribute("mensagemErro", "Código postal inválido.");
                model.addAttribute("codigosPostais", codPostalClienteService.listarTodos());
                return "registar-cliente";
            }

            Cliente cliente = new Cliente();
            cliente.setNomecliente(nomecliente);
            cliente.setTelefonecliente(telefonecliente);
            cliente.setRuacliente(ruacliente);
            cliente.setNportacliente(nportacliente);
            cliente.setNifcliente(nifcliente);
            cliente.setCodpostalcli(codPostal.get());
            cliente.setUsername(username);
            cliente.setPass(pass);

            clienteService.salvar(cliente);
            model.addAttribute("mensagemSucesso", "Conta criada com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao criar conta: " + e.getMessage());
        }

        model.addAttribute("codigosPostais", codPostalClienteService.listarTodos());
        return "registar-cliente";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
