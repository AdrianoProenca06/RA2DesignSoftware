package com.example.caronas.controller;

import com.example.caronas.model.Pagamento;
import com.example.caronas.service.PagamentoService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PagamentoController {
    private PagamentoService service;

    public PagamentoController() {
        this.service = new PagamentoService();
    }

    public Pagamento criar(String solicitacaoCaronaId, double valor, String metodo) throws IOException {
        return service.criar(solicitacaoCaronaId, valor, metodo);
    }

    public List<Pagamento> listar() {
        return service.listar();
    }

    public Optional<Pagamento> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public List<Pagamento> buscarPorSolicitacao(String solicitacaoCaronaId) {
        return service.buscarPorSolicitacao(solicitacaoCaronaId);
    }

    public List<Pagamento> buscarPorStatus(String status) {
        return service.buscarPorStatus(status);
    }

    public List<Pagamento> buscarPorMetodo(String metodo) {
        return service.buscarPorMetodo(metodo);
    }

    public Pagamento realizarPagamento(String id) throws IOException {
        return service.realizarPagamento(id);
    }

    public Pagamento atualizar(String id, String solicitacaoCaronaId, double valor, String metodo, String status) throws IOException {
        return service.atualizar(id, solicitacaoCaronaId, valor, metodo, status);
    }

    public Pagamento cancelarPagamento(String id) throws IOException {
        return service.cancelarPagamento(id);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }
}

