package com.example.projeto2web.controllers;

import com.example.projeto2.models.TipoProjeto;
import com.example.projeto2.repositories.TipoProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
public class GerenteProjetosController {

    @Autowired
    private TipoProjetoRepository tipoProjetoRepo;

    @GetMapping("/adicionar-tipo-projeto")
    public String mostrarFormularioTipoProjeto(Model model) {
        return "adicionar-tipo-projeto";
    }

    @PostMapping("/adicionar-tipo-projeto")
    public String processarNovoTipoProjeto(@RequestParam String tipoprojeto, Model model) {
        if (tipoprojeto == null || tipoprojeto.trim().isEmpty()) {
            model.addAttribute("mensagemErro", "O nome do tipo de projeto não pode ser vazio.");
        } else {
            try {
                TipoProjeto novo = new TipoProjeto();
                novo.setTipoprojeto(tipoprojeto);
                tipoProjetoRepo.save(novo);
                model.addAttribute("mensagemSucesso", "Tipo de projeto adicionado com sucesso!");
            } catch (Exception e) {
                model.addAttribute("mensagemErro", "Erro ao adicionar tipo de projeto: " + e.getMessage());
            }
        }
        return "adicionar-tipo-projeto";
    }

    @GetMapping("/remover-tipo-projeto")
    public String mostrarFormularioRemoverTipoProjeto(Model model) {
        List<TipoProjeto> tipos = tipoProjetoRepo.findAll();
        tipos.sort(Comparator.comparing(TipoProjeto::getTipoprojeto, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("tiposProjetos", tipos);
        return "remover-tipo-projeto";
    }

    @PostMapping("/remover-tipo-projeto")
    public String removerTipoProjeto(@RequestParam("tipoProjetoId") Integer id, Model model) {
        try {
            tipoProjetoRepo.deleteById(id);
            model.addAttribute("mensagemSucesso", "Tipo de projeto removido com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao remover tipo de projeto: " + e.getMessage());
        }

        List<TipoProjeto> tipos = tipoProjetoRepo.findAll();
        tipos.sort(Comparator.comparing(TipoProjeto::getTipoprojeto, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("tiposProjetos", tipos);
        return "remover-tipo-projeto";
    }

    @GetMapping("/listar-tipos-projetos")
    public String listarTiposProjetos(Model model) {
        List<TipoProjeto> tipos = tipoProjetoRepo.findAll();
        tipos.sort(Comparator.comparing(TipoProjeto::getTipoprojeto, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("tiposProjetos", tipos);
        return "listar-tipos-projetos";
    }
}
