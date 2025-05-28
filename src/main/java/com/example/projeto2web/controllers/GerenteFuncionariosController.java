package com.example.projeto2web.controllers;

import com.example.projeto2.models.Funcionario;
import com.example.projeto2.models.TipoFuncionario;
import com.example.projeto2.services.FuncionarioService;
import com.example.projeto2.services.TipoFuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/gerente/funcionarios")
public class GerenteFuncionariosController {

    private final FuncionarioService funcionarioService;
    private final TipoFuncionarioService tipoFuncionarioService;

    @Autowired
    public GerenteFuncionariosController(FuncionarioService funcionarioService,
                                         TipoFuncionarioService tipoFuncionarioService) {
        this.funcionarioService = funcionarioService;
        this.tipoFuncionarioService = tipoFuncionarioService;
    }

    @GetMapping("/adicionar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        model.addAttribute("cargos", tipoFuncionarioService.listarTodos());
        return "adicionar-funcionario";
    }

    @PostMapping("/adicionar")
    public String adicionarFuncionario(@ModelAttribute("funcionario") Funcionario funcionario,
                                       Model model) {
        if (funcionario.getNomefuncionario() == null || funcionario.getNomefuncionario().isEmpty() ||
                funcionario.getUsername() == null || funcionario.getUsername().isEmpty() ||
                funcionario.getPassword() == null || funcionario.getPassword().isEmpty() ||
                funcionario.getIdtipofunc() == null || funcionario.getIdtipofunc().getId() == null) {

            model.addAttribute("erro", "Por favor, preencha todos os campos.");
            model.addAttribute("cargos", tipoFuncionarioService.listarTodos());
            return "adicionar-funcionario";
        }

        try {
            TipoFuncionario tipo = tipoFuncionarioService.procurarPorId(funcionario.getIdtipofunc().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cargo inválido"));

            funcionario.setIdtipofunc(tipo);
            funcionarioService.salvar(funcionario);

            model.addAttribute("sucesso", "Funcionário adicionado com sucesso!");
            model.addAttribute("funcionario", new Funcionario()); // limpa o formulário
            model.addAttribute("cargos", tipoFuncionarioService.listarTodos());
            return "adicionar-funcionario";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao adicionar funcionário: " + e.getMessage());
            model.addAttribute("cargos", tipoFuncionarioService.listarTodos());
            return "adicionar-funcionario";
        }
    }

    @GetMapping("/remover")
    public String mostrarFormularioRemover(Model model) {
        List<Funcionario> funcionariosOrdenados = funcionarioService.listarTodos()
                .stream()
                .sorted(Comparator.comparing(Funcionario::getNomefuncionario, String.CASE_INSENSITIVE_ORDER))
                .toList();

        model.addAttribute("funcionarios", funcionariosOrdenados);
        return "remover-funcionario";
    }

    @PostMapping("/remover")
    public String removerFuncionario(@RequestParam("id") Integer id, Model model) {
        try {
            funcionarioService.excluir(id);
            model.addAttribute("sucesso", "Funcionário removido com sucesso!");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao remover funcionário: " + e.getMessage());
        }
        model.addAttribute("funcionarios", funcionarioService.listarTodos());
        return "remover-funcionario";
    }

    @GetMapping("/editar")
    public String mostrarEditarFuncionario(@RequestParam(name = "id", required = false) Integer id, Model model) {
        List<Funcionario> funcionariosOrdenados = funcionarioService.listarTodos()
                .stream()
                .sorted(Comparator.comparing(Funcionario::getNomefuncionario, String.CASE_INSENSITIVE_ORDER))
                .toList();

        model.addAttribute("funcionarios", funcionariosOrdenados);
        model.addAttribute("cargos", tipoFuncionarioService.listarTodos());

        if (id != null) {
            funcionarioService.procurarPorId(id).ifPresent(func -> model.addAttribute("funcionarioSelecionado", func));
        }

        return "editar-funcionario";
    }

    @PostMapping("/editar")
    public String editarFuncionario(@RequestParam("id") Integer id,
                                    @RequestParam("nomefuncionario") String nomefuncionario,
                                    @RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam("idtipofunc") Integer idtipofunc,
                                    RedirectAttributes redirectAttrs) {

        if (nomefuncionario == null || nomefuncionario.isEmpty() ||
                username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                idtipofunc == null) {
            redirectAttrs.addFlashAttribute("erro", "Por favor, preencha todos os campos.");
            return "redirect:/gerente/funcionarios/editar?id=" + id;
        }

        try {
            Funcionario funcionario = funcionarioService.procurarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

            TipoFuncionario tipoFuncionario = tipoFuncionarioService.procurarPorId(idtipofunc)
                    .orElseThrow(() -> new RuntimeException("Cargo inválido"));

            funcionario.setNomefuncionario(nomefuncionario);
            funcionario.setUsername(username);
            funcionario.setPassword(password);
            funcionario.setIdtipofunc(tipoFuncionario);

            funcionarioService.atualizar(id, funcionario);

            redirectAttrs.addFlashAttribute("sucesso", "Funcionário atualizado com sucesso!");
            return "redirect:/gerente/funcionarios/editar?id=" + id;

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("erro", "Erro ao atualizar funcionário: " + e.getMessage());
            return "redirect:/gerente/funcionarios/editar?id=" + id;
        }
    }
}
