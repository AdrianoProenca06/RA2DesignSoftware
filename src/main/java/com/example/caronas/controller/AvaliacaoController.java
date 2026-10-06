package com.example.caronas.controller;

import com.example.caronas.model.Avaliacao;
import com.example.caronas.service.AvaliacaoService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AvaliacaoController {
    private AvaliacaoService service;

    public AvaliacaoController() {
        this.service = new AvaliacaoService();
    }

    public Avaliacao criar(String avaliadorId, String usuarioAvaliadoId, int nota, String comentario) throws IOException {
        return service.criar(avaliadorId, usuarioAvaliadoId, nota, comentario);
    }

    public List<Avaliacao> listar() {
        return service.listar();
    }

    public Optional<Avaliacao> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public List<Avaliacao> buscarPorUsuarioAvaliado(String usuarioAvaliadoId) {
        return service.buscarPorUsuarioAvaliado(usuarioAvaliadoId);
    }

    public List<Avaliacao> buscarPorAvaliador(String avaliadorId) {
        return service.buscarPorAvaliador(avaliadorId);
    }

    public double calcularMediaAvaliacoes(String usuarioAvaliadoId) {
        return service.calcularMediaAvaliacoes(usuarioAvaliadoId);
    }

    public Avaliacao atualizar(String id, String avaliadorId, String usuarioAvaliadoId, int nota, String comentario) throws IOException {
        return service.atualizar(id, avaliadorId, usuarioAvaliadoId, nota, comentario);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }
}

