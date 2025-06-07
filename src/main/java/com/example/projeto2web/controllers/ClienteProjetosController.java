package com.example.projeto2web.controllers;

import com.example.projeto2.models.Cliente;
import com.example.projeto2.models.Projeto;
import com.example.projeto2.models.Servico;
import com.example.projeto2.models.TipoProjeto;
import com.example.projeto2.services.ClienteService;
import com.example.projeto2.services.ProjetoService;
import com.example.projeto2.services.ServicoService;
import com.example.projeto2.services.TipoProjetoService;
import com.example.projeto2web.utils.SessaoUtilizador;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cliente/projetos")
public class ClienteProjetosController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private TipoProjetoService tipoProjetoService;

    @Autowired
    private ServicoService servicoService;


    @GetMapping
    public String listarProjetos(HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) {
            return "redirect:/cliente/login";
        }

        return "cliente-projetos";
    }

    @GetMapping("/novo")
    public String mostrarFormulario(HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) {
            return "redirect:/cliente/login";
        }

        model.addAttribute("projeto", new Projeto());
        model.addAttribute("tiposProjeto", tipoProjetoService.listarTodos());
        return "adicionar-projeto";
    }

    @PostMapping("/adicionar")
    public String adicionarProjeto(@ModelAttribute Projeto projeto, HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) {
            return "redirect:/cliente/login";
        }

        try {
            projeto.setIdclien(cliente);
            projetoService.salvarProjeto(projeto);
            model.addAttribute("mensagemSucesso", "Projeto criado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao criar o projeto. Verifique os campos.");
        }

        model.addAttribute("projeto", new Projeto());
        model.addAttribute("tiposProjeto", tipoProjetoService.listarTodos());

        return "adicionar-projeto";
    }

    @GetMapping("/remover")
    public String mostrarFormularioRemocao(HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) {
            return "redirect:/cliente/login";
        }

        List<Projeto> projetosDoCliente = projetoService.listarTodos().stream()
                .filter(p -> p.getIdclien().getId().equals(cliente.getId()))
                .toList();

        model.addAttribute("projetos", projetosDoCliente);
        return "remover-projeto";
    }

    @PostMapping("/remover")
    public String removerProjeto(@RequestParam("id") Integer projetoId, HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) {
            return "redirect:/cliente/login";
        }

        try {
            Projeto projeto = projetoService.procurarPorId(projetoId).orElse(null);
            if (projeto != null && projeto.getIdclien().getId().equals(cliente.getId())) {
                projetoService.excluirProjeto(projetoId);
                model.addAttribute("mensagemSucesso", "Projeto removido com sucesso!");
            } else {
                model.addAttribute("mensagemErro", "Projeto não encontrado ou não pertence a si.");
            }
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao remover o projeto.");
        }

        List<Projeto> projetosAtualizados = projetoService.listarTodos().stream()
                .filter(p -> p.getIdclien().getId().equals(cliente.getId()))
                .toList();

        model.addAttribute("projetos", projetosAtualizados);
        return "remover-projeto";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEdicao(HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) return "redirect:/cliente/login";

        List<Projeto> projetosDoCliente = projetoService.listarTodos().stream()
                .filter(p -> p.getIdclien().getId().equals(cliente.getId()))
                .toList();

        model.addAttribute("projetos", projetosDoCliente);
        model.addAttribute("tiposProjeto", tipoProjetoService.listarTodos());

        return "editar-projeto";
    }


    @PostMapping("/editar")
    public String editarProjeto(@ModelAttribute Projeto projeto, HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) {
            return "redirect:/cliente/login";
        }

        try {
            projeto.setIdclien(cliente);
            projetoService.salvarProjeto(projeto); // Atualiza porque o ID já existe
            model.addAttribute("mensagemSucesso", "Projeto editado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao editar o projeto.");
        }

        model.addAttribute("projeto", projeto);
        model.addAttribute("tiposProjeto", tipoProjetoService.listarTodos());

        return "editar-projeto";
    }

    @GetMapping("/listar")
    public String listarProjetosCliente(HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) return "redirect:/cliente/login";

        List<Projeto> projetosDoCliente = projetoService.listarTodos().stream()
                .filter(p -> p.getIdclien().getId().equals(cliente.getId()))
                .toList();

        model.addAttribute("projetos", projetosDoCliente);
        return "listar-projetos-cliente";
    }

    @GetMapping("/estado")
    public String verEstadoProjeto(@RequestParam(name = "id", required = false) Integer projetoId,
                                   HttpSession session, Model model) {
        Cliente cliente = SessaoUtilizador.getCliente(session);
        if (cliente == null) {
            return "redirect:/cliente/login";
        }

        List<Projeto> projetosDoCliente = projetoService.listarTodos().stream()
                .filter(p -> p.getIdclien().getId().equals(cliente.getId()))
                .toList();
        model.addAttribute("projetos", projetosDoCliente);

        if (projetoId != null) {
            Servico servico = servicoService.listarTodos().stream()
                    .filter(s -> s.getIdproj().getId().equals(projetoId)) // Usa getIdproj em Servico
                    .findFirst()
                    .orElse(null);

            if (servico != null) {
                model.addAttribute("estado", servico.getEstadoservico());
            } else {
                model.addAttribute("estado", "Sem estado disponível");
            }
        }

        return "estado-projeto";
    }

}
