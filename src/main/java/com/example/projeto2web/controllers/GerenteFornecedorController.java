package com.example.projeto2web.controllers;

import com.example.projeto2.models.CodPostalFornecedor;
import com.example.projeto2.models.Fornecedor;
import com.example.projeto2.repositories.CodPostalFornecedorRepository;
import com.example.projeto2.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
public class GerenteFornecedorController {

    @Autowired
    private FornecedorRepository fornecedorRepo;

    @Autowired
    private CodPostalFornecedorRepository codPostalRepo;

    @GetMapping("/adicionar-fornecedor")
    public String mostrarFormularioFornecedor(Model model) {
        List<CodPostalFornecedor> codigos = codPostalRepo.findAll();
        codigos.sort(Comparator.comparing(CodPostalFornecedor::getCodpostalfornecedor, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("codigosPostais", codigos);
        return "adicionar-fornecedor";
    }

    @PostMapping("/adicionar-fornecedor")
    public String salvarFornecedor(@RequestParam String nome,
                                   @RequestParam String nif,
                                   @RequestParam String telefone,
                                   @RequestParam String rua,
                                   @RequestParam Integer porta,
                                   @RequestParam String codPostalId,
                                   Model model) {
        try {
            CodPostalFornecedor codigoPostal = codPostalRepo.findById(codPostalId).orElse(null);
            if (codigoPostal == null) {
                model.addAttribute("mensagemErro", "Código postal inválido.");
            } else {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setNomefornecedor(nome);
                fornecedor.setNiffornecedor(nif);
                fornecedor.setTelefonefornecedor(telefone);
                fornecedor.setRuafornecedor(rua);
                fornecedor.setNportafornecedor(porta);
                fornecedor.setCodpostalforn(codigoPostal);

                fornecedorRepo.save(fornecedor);
                model.addAttribute("mensagemSucesso", "Fornecedor adicionado com sucesso!");
            }
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao adicionar fornecedor: " + e.getMessage());
        }

        List<CodPostalFornecedor> codigos = codPostalRepo.findAll();
        codigos.sort(Comparator.comparing(CodPostalFornecedor::getCodpostalfornecedor, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("codigosPostais", codigos);

        return "adicionar-fornecedor";
    }

    @GetMapping("/remover-fornecedor")
    public String mostrarRemoverFornecedor(Model model) {
        List<Fornecedor> fornecedores = fornecedorRepo.findAll();
        fornecedores.sort((f1, f2) -> f1.getNomefornecedor().compareToIgnoreCase(f2.getNomefornecedor()));
        model.addAttribute("fornecedores", fornecedores);
        return "remover-fornecedor";
    }

    @PostMapping("/remover-fornecedor")
    public String processarRemocaoFornecedor(@RequestParam Integer id, Model model) {
        try {
            if (fornecedorRepo.existsById(id)) {
                fornecedorRepo.deleteById(id);
                model.addAttribute("mensagemSucesso", "Fornecedor removido com sucesso!");
            } else {
                model.addAttribute("mensagemErro", "Fornecedor não encontrado.");
            }
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao remover fornecedor: " + e.getMessage());
        }

        List<Fornecedor> fornecedores = fornecedorRepo.findAll();
        fornecedores.sort((f1, f2) -> f1.getNomefornecedor().compareToIgnoreCase(f2.getNomefornecedor()));
        model.addAttribute("fornecedores", fornecedores);
        return "remover-fornecedor";
    }

    @GetMapping("/listar-fornecedores")
    public String listarFornecedores(Model model) {
        List<Fornecedor> fornecedores = fornecedorRepo.findAll();
        fornecedores.sort((f1, f2) -> f1.getNomefornecedor().compareToIgnoreCase(f2.getNomefornecedor()));
        model.addAttribute("fornecedores", fornecedores);
        return "listar-fornecedores";
    }
}
