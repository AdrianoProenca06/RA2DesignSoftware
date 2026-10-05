package com.example.caronas.controller;

import com.example.caronas.facade.CaronasFacade;
import com.example.caronas.model.Pagamento;
import com.example.caronas.service.PagamentoService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PagamentoController {
    private PagamentoService service;
    private CaronasFacade facade;

    public PagamentoController() {
        this.service = new PagamentoService();
        this.facade = new CaronasFacade();
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

    /** Paga pelo Facade: valida a solicitação aceita e dispara os eventos (histórico, notificação). */
    public Pagamento realizarPagamento(String id, String metodo) throws IOException {
        return facade.pagar(id, metodo);
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

