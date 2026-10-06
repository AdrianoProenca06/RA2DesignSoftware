package com.example.caronas.controller;

import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.service.SolicitacaoCaronaService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SolicitacaoCaronaController {
    private SolicitacaoCaronaService service;

    public SolicitacaoCaronaController() {
        this.service = new SolicitacaoCaronaService();
    }

    public SolicitacaoCarona criar(String passageiroId, String caronaId) throws IOException {
        return service.criar(passageiroId, caronaId);
    }

    public List<SolicitacaoCarona> listar() {
        return service.listar();
    }

    public Optional<SolicitacaoCarona> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public List<SolicitacaoCarona> buscarPorPassageiro(String passageiroId) {
        return service.buscarPorPassageiro(passageiroId);
    }

    public List<SolicitacaoCarona> buscarPorCarona(String caronaId) {
        return service.buscarPorCarona(caronaId);
    }

    public List<SolicitacaoCarona> buscarPorStatus(String status) {
        return service.buscarPorStatus(status);
    }

    public SolicitacaoCarona aceitar(String id) throws IOException {
        return service.aceitar(id);
    }

    public SolicitacaoCarona atualizar(String id, String passageiroId, String caronaId, String status) throws IOException {
        return service.atualizar(id, passageiroId, caronaId, status);
    }

    public SolicitacaoCarona recusar(String id) throws IOException {
        return service.recusar(id);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }
}

