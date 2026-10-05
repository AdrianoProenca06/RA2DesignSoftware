package com.example.caronas.controller;

import com.example.caronas.model.Perfil;
import com.example.caronas.service.PerfilService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PerfilController {
    private PerfilService service;

    public PerfilController() {
        this.service = new PerfilService();
    }

    public Perfil criar(String usuarioId, String curso, String turno, String telefone) throws IOException {
        return service.criar(usuarioId, curso, turno, telefone);
    }

    public List<Perfil> listar() {
        return service.listar();
    }

    public Optional<Perfil> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public Optional<Perfil> buscarPorUsuarioId(String usuarioId) {
        return service.buscarPorUsuarioId(usuarioId);
    }

    public List<Perfil> buscarPorCurso(String curso) {
        return service.buscarPorCurso(curso);
    }

    public Perfil atualizar(String id, String curso, String turno, String telefone, double avaliacaoMedia) throws IOException {
        return service.atualizar(id, curso, turno, telefone, avaliacaoMedia);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }
}

