package com.example.projeto2web.controllers;

import com.example.projeto2.models.FaturaCliente;
import com.example.projeto2.models.Servico;
import com.example.projeto2.repositories.FaturaClienteRepository;
import com.example.projeto2.repositories.ServicoRepository;
import com.example.projeto2web.utils.SessaoUtilizador;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/funcionario2/cliente")
public class FuncionarioPagamentoClienteController {

    @Autowired
    private ServicoRepository servicoRepo;

    @Autowired
    private FaturaClienteRepository faturaRepo;

    @Autowired
    private FaturaClienteRepository faturaClienteRepository;

    @GetMapping("/registar-pagamento-cliente")
    public String mostrarFormularioPagamento(Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) return "redirect:/funcionario";

        List<Servico> servicos = servicoRepo.findAll();
        model.addAttribute("servicos", servicos);
        return "registar-pagamento-cliente";
    }

    @PostMapping("/registar-pagamento-cliente")
    public String registarPagamento(@RequestParam("valor") double valor,
                                    @RequestParam("data") String data,
                                    @RequestParam("servicoId") int servicoId,
                                    Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) return "redirect:/funcionario";

        try {
            Servico servico = servicoRepo.findById(servicoId).orElse(null);
            if (servico == null) {
                model.addAttribute("mensagemErro", "Serviço não encontrado.");
                return mostrarFormularioPagamento(model, session);
            }

            FaturaCliente fatura = new FaturaCliente();
            fatura.setServico(servico);
            fatura.setValorfatura(valor);
            fatura.setDatafatura(LocalDate.parse(data));

            faturaRepo.save(fatura);
            model.addAttribute("mensagemSucesso", "Pagamento registado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao registar pagamento: " + e.getMessage());
        }

        return mostrarFormularioPagamento(model, session);
    }

    @GetMapping("/listar-pagamentos-clientes")
    public String listarPagamentosCliente(Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) return "redirect:/funcionario";

        model.addAttribute("faturas", faturaClienteRepository.findAll());
        return "listar-pagamentos-clientes";
    }

}
