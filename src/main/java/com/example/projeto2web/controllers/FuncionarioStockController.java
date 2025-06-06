package com.example.projeto2web.controllers;

import com.example.projeto2.models.Material;
import com.example.projeto2.repositories.MaterialRepository;
import com.example.projeto2web.utils.SessaoUtilizador;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/funcionario2/stock")
public class FuncionarioStockController {

    @Autowired
    private MaterialRepository materialRepo;

    @GetMapping("/listar")
    public String listarStock(Model model, HttpSession session) {
        if (SessaoUtilizador.getFuncionario(session) == null) {
            return "redirect:/funcionario";
        }

        List<Material> materiais = materialRepo.findAll();
        materiais.sort(Comparator.comparing(Material::getNomematerial, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("materiais", materiais);
        return "listar-stock-funcionario";
    }
}
