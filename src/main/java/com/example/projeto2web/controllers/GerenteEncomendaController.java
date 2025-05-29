package com.example.projeto2web.controllers;

import com.example.projeto2.models.Fornecedor;
import com.example.projeto2.models.LinhaEncomenda;
import com.example.projeto2.models.LinhaEncomendaId;
import com.example.projeto2.models.Material;
import com.example.projeto2.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
public class GerenteEncomendaController {

    @Autowired
    private MaterialRepository materialRepo;

    @Autowired
    private FornecedorRepository fornecedorRepo;

    @Autowired
    private LinhaEncomendaRepository linhaRepo;


    @GetMapping("/encomendar")
    public String mostrarFormularioEncomenda(Model model) {
        List<Material> materiais = materialRepo.findAll();
        materiais.sort(Comparator.comparing(Material::getNomematerial, String.CASE_INSENSITIVE_ORDER));

        List<Fornecedor> fornecedores = fornecedorRepo.findAll();
        fornecedores.sort(Comparator.comparing(Fornecedor::getNomefornecedor, String.CASE_INSENSITIVE_ORDER));

        model.addAttribute("materiais", materiais);
        model.addAttribute("fornecedores", fornecedores);
        return "encomendar-material";
    }

    @PostMapping("/encomendar")
    public String processarEncomenda(@RequestParam Integer materialId,
                                     @RequestParam Integer fornecedorId,
                                     @RequestParam int quantidade,
                                     Model model) {

        Material m = materialRepo.findById(materialId).orElse(null);
        Fornecedor f = fornecedorRepo.findById(fornecedorId).orElse(null);

        if (m != null && f != null && quantidade > 0) {
            double total = m.getPrecomaterial() * quantidade;

            LinhaEncomenda le = new LinhaEncomenda();
            le.setId(new LinhaEncomendaId(m.getId(), f.getId()));
            le.setIdmaterial(m);
            le.setIdfornecedor(f);
            le.setQtdencomendada(quantidade);
            le.setValorencomendado(total);
            linhaRepo.save(le);

            model.addAttribute("mensagemSucesso", "Encomenda realizada com sucesso!");
        } else {
            model.addAttribute("mensagemErro", "Erro na encomenda. Verifique os dados.");
        }

        model.addAttribute("materiais", materialRepo.findAll());
        model.addAttribute("fornecedores", fornecedorRepo.findAll());

        return "encomendar-material";
    }

    @GetMapping("/cancelar-encomenda")
    public String mostrarFormularioCancelamento(Model model) {
        List<LinhaEncomenda> encomendas = linhaRepo.findAll();
        encomendas.sort(Comparator.comparing(e -> e.getIdmaterial().getNomematerial(), String.CASE_INSENSITIVE_ORDER));

        model.addAttribute("encomendas", encomendas);
        return "cancelar-encomenda";
    }

    @PostMapping("/cancelar-encomenda")
    public String processarCancelamento(@RequestParam("encomendaId") String idComposto, Model model) {
        try {
            String[] ids = idComposto.split("-");
            Integer materialId = Integer.parseInt(ids[0]);
            Integer fornecedorId = Integer.parseInt(ids[1]);

            LinhaEncomendaId id = new LinhaEncomendaId(materialId, fornecedorId);
            if (linhaRepo.existsById(id)) {
                linhaRepo.deleteById(id);
                model.addAttribute("mensagemSucesso", "Encomenda cancelada com sucesso!");
            } else {
                model.addAttribute("mensagemErro", "Encomenda não encontrada.");
            }
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao cancelar a encomenda.");
        }

        List<LinhaEncomenda> encomendas = linhaRepo.findAll();
        encomendas.sort(Comparator.comparing(e -> e.getIdmaterial().getNomematerial(), String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("encomendas", encomendas);
        return "cancelar-encomenda";
    }

    @GetMapping("/listar-encomenda")
    public String listarEncomendas(Model model) {
        List<LinhaEncomenda> encomendas = linhaRepo.findAll();
        encomendas.sort(Comparator.comparing(e -> e.getIdmaterial().getNomematerial(), String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("encomendas", encomendas);
        return "listar-encomenda";
    }

}
