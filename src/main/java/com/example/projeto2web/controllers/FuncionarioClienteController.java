package com.example.projeto2web.controllers;

import com.example.projeto2.models.Funcionario;
import com.example.projeto2.models.Material;
import com.example.projeto2.models.Projeto;
import com.example.projeto2.models.Servico;
import com.example.projeto2.models.TipoImpressao;
import com.example.projeto2.repositories.FuncionarioRepository;
import com.example.projeto2.repositories.MaterialRepository;
import com.example.projeto2.repositories.ProjetoRepository;
import com.example.projeto2.repositories.ServicoRepository;
import com.example.projeto2.repositories.TipoImpressaoRepository;
import com.example.projeto2web.utils.SessaoUtilizador;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/funcionario2/cliente")
public class FuncionarioClienteController {

    @Autowired
    private ProjetoRepository projetoRepo;

    @Autowired
    private FuncionarioRepository funcionarioRepo;

    @Autowired
    private TipoImpressaoRepository tipoRepo;

    @Autowired
    private MaterialRepository materialRepo;

    @Autowired
    private ServicoRepository servicoRepo;

    @GetMapping("/listar-projetos")
    public String listarProjetos(Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) return "redirect:/funcionario";

        model.addAttribute("projetos", projetoRepo.findAll());
        return "listar-projetos";
    }

    @GetMapping("/adicionar-servico")
    public String mostrarFormularioAdicionar(Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) return "redirect:/funcionario";

        model.addAttribute("projetos", projetoRepo.findAll());
        model.addAttribute("funcionarios", funcionarioRepo.findAll());
        model.addAttribute("tiposImpressao", tipoRepo.findAll());
        model.addAttribute("materiais", materialRepo.findAll());
        return "adicionar-servico";
    }

    @PostMapping("/adicionar-servico")
    public String adicionarServico(@RequestParam("data") String data,
                                   @RequestParam("preco") double preco,
                                   @RequestParam("estado") String estado,
                                   @RequestParam("projetoId") int projetoId,
                                   @RequestParam("funcionarioId") int funcionarioId,
                                   @RequestParam("tipoImpressaoId") int tipoImpressaoId,
                                   @RequestParam("materialIds") List<Integer> materialIds,
                                   @RequestParam("quantidades") List<Integer> quantidades,
                                   Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) return "redirect:/funcionario";

        try {
            Servico servico = new Servico();
            servico.setDataservico(LocalDate.parse(data));
            servico.setPrecoservico(preco);
            servico.setEstadoservico(estado);
            servico.setIdproj(projetoRepo.findById(projetoId).orElse(null));
            servico.setIdfuncio(funcionarioRepo.findById(funcionarioId).orElse(null));
            servico.setIdtipoimpre(tipoRepo.findById(tipoImpressaoId).orElse(null));

            servicoRepo.save(servico);

            for (int i = 0; i < materialIds.size(); i++) {
                Material mat = materialRepo.findById(materialIds.get(i)).orElse(null);
                if (mat != null) {
                    int quantidade = quantidades.get(i);
                    mat.setQtdstockmaterial(mat.getQtdstockmaterial() - quantidade);
                    materialRepo.save(mat);
                }
            }

            model.addAttribute("mensagemSucesso", "Serviço adicionado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao adicionar serviço: " + e.getMessage());
        }

        return mostrarFormularioAdicionar(model, session);
    }

    @GetMapping("/remover-servico")
    public String listarParaRemover(Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) return "redirect:/funcionario";

        model.addAttribute("servicos", servicoRepo.findAll());
        return "remover-servico";
    }

    @PostMapping("/remover-servico")
    public String removerServico(@RequestParam("servicoId") int id, Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) return "redirect:/funcionario";

        try {
            servicoRepo.deleteById(id);
            model.addAttribute("mensagemSucesso", "Serviço removido com sucesso.");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao remover serviço: " + e.getMessage());
        }

        return listarParaRemover(model, session);
    }

    @GetMapping("/listar-servicos")
    public String listarServicos(Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) {
            return "redirect:/funcionario";
        }

        List<Servico> servicos = servicoRepo.findAll();
        model.addAttribute("servicos", servicos);
        return "listar-servicos";
    }
}
