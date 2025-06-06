package com.example.projeto2web.controllers;

import com.example.projeto2.models.TipoImpressao;
import com.example.projeto2.repositories.TipoImpressaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
public class GerenteImpressoesController {

    @Autowired
    private TipoImpressaoRepository tipoImpressaoRepo;

    @GetMapping("/adicionar-tipo-impressao")
    public String mostrarFormularioAdicionarTipoImpressao(Model model) {
        return "adicionar-tipo-impressao";
    }

    @PostMapping("/adicionar-tipo-impressao")
    public String processarAdicionarTipoImpressao(@RequestParam String tipoImpressao, Model model) {
        if (tipoImpressao == null || tipoImpressao.trim().isEmpty()) {
            model.addAttribute("mensagemErro", "O tipo de impressão não pode ser vazio.");
        } else {
            try {
                TipoImpressao novo = new TipoImpressao();
                novo.setTipoimpressao(tipoImpressao);
                tipoImpressaoRepo.save(novo);
                model.addAttribute("mensagemSucesso", "Tipo de impressão adicionado com sucesso!");
            } catch (Exception e) {
                model.addAttribute("mensagemErro", "Erro ao adicionar tipo de impressão: " + e.getMessage());
            }
        }
        return "adicionar-tipo-impressao";
    }

    @GetMapping("/remover-tipo-impressao")
    public String mostrarFormularioRemoverTipoImpressao(Model model) {
        List<TipoImpressao> tipos = tipoImpressaoRepo.findAll();
        tipos.sort(Comparator.comparing(TipoImpressao::getTipoimpressao, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("tiposImpressao", tipos);
        return "remover-tipo-impressao";
    }

    @PostMapping("/remover-tipo-impressao")
    public String processarRemoverTipoImpressao(@RequestParam("tipoImpressaoId") Integer id, Model model) {
        try {
            tipoImpressaoRepo.deleteById(id);
            model.addAttribute("mensagemSucesso", "Tipo de impressão removido com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao remover tipo de impressão: " + e.getMessage());
        }

        List<TipoImpressao> tipos = tipoImpressaoRepo.findAll();
        tipos.sort(Comparator.comparing(TipoImpressao::getTipoimpressao, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("tiposImpressao", tipos);
        return "remover-tipo-impressao";
    }

    @GetMapping("/listar-tipos-impressoes")
    public String listarTiposImpressoes(Model model) {
        List<TipoImpressao> tipos = tipoImpressaoRepo.findAll();
        tipos.sort(Comparator.comparing(TipoImpressao::getTipoimpressao, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("tiposImpressao", tipos);
        return "listar-tipos-impressoes";
    }
}
