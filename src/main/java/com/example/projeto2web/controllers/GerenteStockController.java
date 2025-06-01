package com.example.projeto2web.controllers;

import com.example.projeto2.models.Material;
import com.example.projeto2.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
public class GerenteStockController {

    @Autowired
    private MaterialRepository materialRepo;

    @GetMapping("/listar-stock")
    public String listarStock(Model model) {
        List<Material> materiais = materialRepo.findAll();
        materiais.sort(Comparator.comparing(Material::getNomematerial, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("materiais", materiais);
        return "listar-stock";
    }

    @GetMapping("/adicionar-produto")
    public String mostrarFormularioAdicionar() {
        return "adicionar-produto";
    }

    @PostMapping("/adicionar-produto")
    public String processarAdicaoProduto(@RequestParam String nome,
                                         @RequestParam double preco,
                                         @RequestParam int quantidade,
                                         Model model) {
        try {
            Material material = new Material();
            material.setNomematerial(nome);
            material.setPrecomaterial(preco);
            material.setQtdstockmaterial(quantidade);
            materialRepo.save(material);

            model.addAttribute("mensagemSucesso", "Produto adicionado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao adicionar produto.");
        }

        return "adicionar-produto";
    }

    @GetMapping("/remover-produto")
    public String mostrarRemoverProduto(Model model) {
        List<Material> materiais = materialRepo.findAll();
        materiais.sort(Comparator.comparing(Material::getNomematerial, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("materiais", materiais);
        return "remover-produto";
    }

    @PostMapping("/remover-produto")
    public String removerProduto(@RequestParam Integer id, Model model) {
        try {
            if (materialRepo.existsById(id)) {
                materialRepo.deleteById(id);
                model.addAttribute("mensagemSucesso", "Produto removido com sucesso!");
            } else {
                model.addAttribute("mensagemErro", "Produto não encontrado.");
            }
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao remover o produto: " + e.getMessage());
        }

        List<Material> materiais = materialRepo.findAll();
        materiais.sort(Comparator.comparing(Material::getNomematerial, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("materiais", materiais);
        return "remover-produto";
    }

    @GetMapping("/editar-produto")
    public String mostrarEditarProduto(Model model) {
        List<Material> materiais = materialRepo.findAll();
        materiais.sort(Comparator.comparing(Material::getNomematerial, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("materiais", materiais);
        return "editar-produto";
    }

    @PostMapping("/editar-produto")
    public String editarProduto(@RequestParam Integer id,
                                @RequestParam String nome,
                                @RequestParam double preco,
                                @RequestParam int quantidade,
                                Model model) {
        try {
            Material material = materialRepo.findById(id).orElse(null);
            if (material == null) {
                model.addAttribute("mensagemErro", "Produto não encontrado.");
            } else {
                material.setNomematerial(nome);
                material.setPrecomaterial(preco);
                material.setQtdstockmaterial(quantidade);
                materialRepo.save(material);
                model.addAttribute("mensagemSucesso", "Produto editado com sucesso!");
            }
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao editar o produto: " + e.getMessage());
        }

        List<Material> materiais = materialRepo.findAll();
        materiais.sort(Comparator.comparing(Material::getNomematerial, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("materiais", materiais);
        return "editar-produto";
    }

}
