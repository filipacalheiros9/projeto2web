package com.example.projeto2web.controllers;

import com.example.projeto2.models.Fornecedor;
import com.example.projeto2.models.PagamentoFaturaFornecedor;
import com.example.projeto2.repositories.FornecedorRepository;
import com.example.projeto2.repositories.PagamentoFaturaFornecedorRepository;
import com.example.projeto2.services.PagamentoFaturaFornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
public class GerentePagamentoFornecedorController {

    @Autowired
    private FornecedorRepository fornecedorRepo;

    @Autowired
    private PagamentoFaturaFornecedorService pagamentoService;

    @Autowired
    private PagamentoFaturaFornecedorRepository pagamentoRepo;

    @GetMapping("/pagamento-fornecedor")
    public String mostrarFormulario(Model model) {
        List<Fornecedor> fornecedores = fornecedorRepo.findAll();
        fornecedores.sort(Comparator.comparing(Fornecedor::getNomefornecedor, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("fornecedores", fornecedores);
        return "pagamento-fornecedor";
    }

    @PostMapping("/pagamento-fornecedor")
    public String processarPagamento(@RequestParam Integer fornecedorId,
                                     @RequestParam Double valor,
                                     @RequestParam String dataFatura,
                                     Model model) {
        try {
            Fornecedor fornecedor = fornecedorRepo.findById(fornecedorId).orElse(null);
            if (fornecedor == null) {
                model.addAttribute("mensagemErro", "Fornecedor não encontrado.");
            } else {
                pagamentoService.realizarPagamento(fornecedor, valor, LocalDate.parse(dataFatura));
                model.addAttribute("mensagemSucesso", "Pagamento realizado com sucesso!");
            }
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao realizar o pagamento: " + e.getMessage());
        }

        List<Fornecedor> fornecedores = fornecedorRepo.findAll();
        fornecedores.sort(Comparator.comparing(Fornecedor::getNomefornecedor, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("fornecedores", fornecedores);

        return "pagamento-fornecedor";
    }

    @GetMapping("/listar-pagamentos-fornecedores")
    public String listarPagamentos(Model model) {
        List<PagamentoFaturaFornecedor> pagamentos = pagamentoRepo.findAll();
        pagamentos.sort(Comparator.comparing(p -> p.getIdforn().getNomefornecedor(), String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("pagamentos", pagamentos);
        return "listar-pagamentos-fornecedores";
    }
}
