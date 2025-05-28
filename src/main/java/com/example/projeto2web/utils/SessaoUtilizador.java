package com.example.projeto2web.utils;

import com.example.projeto2.models.Funcionario;
import jakarta.servlet.http.HttpSession;

public class SessaoUtilizador {

    private static final String ATTR_FUNCIONARIO = "funcionarioLogado";

    public static void setFuncionario(HttpSession session, Funcionario f) {
        session.setAttribute(ATTR_FUNCIONARIO, f);
    }

    public static Funcionario getFuncionario(HttpSession session) {
        return (Funcionario) session.getAttribute(ATTR_FUNCIONARIO);
    }

    public static void limpar(HttpSession session) {
        session.removeAttribute(ATTR_FUNCIONARIO);
    }
}
