package com.example.projeto2web.utils;

import com.example.projeto2.models.Funcionario;
import com.example.projeto2.models.Cliente;
import jakarta.servlet.http.HttpSession;

public class SessaoUtilizador {

    private static final String ATTR_FUNCIONARIO = "funcionarioLogado";
    private static final String ATTR_CLIENTE = "clienteLogado";

    public static void setFuncionario(HttpSession session, Funcionario f) {
        session.setAttribute(ATTR_FUNCIONARIO, f);
    }

    public static Funcionario getFuncionario(HttpSession session) {
        return (Funcionario) session.getAttribute(ATTR_FUNCIONARIO);
    }

    public static void limparFuncionario(HttpSession session) {
        session.removeAttribute(ATTR_FUNCIONARIO);
    }

    public static void setCliente(HttpSession session, Cliente c) {
        session.setAttribute(ATTR_CLIENTE, c);
    }

    public static Cliente getCliente(HttpSession session) {
        return (Cliente) session.getAttribute(ATTR_CLIENTE);
    }

    public static void limparCliente(HttpSession session) {
        session.removeAttribute(ATTR_CLIENTE);
    }

    public static void limparTudo(HttpSession session) {
        session.invalidate();
    }
}
