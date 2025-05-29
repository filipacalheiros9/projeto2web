package com.example.projeto2web.controllers;

import com.example.projeto2.models.Material;
import com.example.projeto2.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
