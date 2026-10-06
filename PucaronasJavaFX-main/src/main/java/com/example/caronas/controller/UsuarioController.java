package com.example.caronas.controller;

import com.example.caronas.model.Usuario;
import com.example.caronas.service.UsuarioService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UsuarioController {
    private UsuarioService service;

    public UsuarioController() {
        this.service = new UsuarioService();
    }

    public Usuario criar(String nome, String email, String senha, String tipo) throws IOException {
        return service.criar(nome, email, senha, tipo);
    }

    public List<Usuario> listar() {
        return service.listar();
    }

    public Optional<Usuario> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return service.buscarPorEmail(email);
    }

    public List<Usuario> buscarPorTipo(String tipo) {
        return service.buscarPorTipo(tipo);
    }

    public Usuario atualizar(String id, String nome, String email, String senha, String tipo) throws IOException {
        return service.atualizar(id, nome, email, senha, tipo);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }

    public boolean autenticar(String email, String senha) {
        return service.autenticar(email, senha);
    }
}

