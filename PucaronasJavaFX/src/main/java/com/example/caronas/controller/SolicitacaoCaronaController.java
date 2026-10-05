package com.example.caronas.controller;

import com.example.caronas.facade.CaronasFacade;
import com.example.caronas.model.Pagamento;
import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.service.SolicitacaoCaronaService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SolicitacaoCaronaController {
    private SolicitacaoCaronaService service;
    private CaronasFacade facade;

    public SolicitacaoCaronaController() {
        this.service = new SolicitacaoCaronaService();
        this.facade = new CaronasFacade();
    }

    // Operações com regras de negócio passam pelo Facade; consultas simples continuam no serviço.

    public SolicitacaoCarona criar(String passageiroId, String caronaId) throws IOException {
        return facade.solicitarCarona(passageiroId, caronaId);
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

    /** Aceita a solicitação e devolve o pagamento pendente gerado pela política de preço da carona. */
    public Pagamento aceitar(String id) throws IOException {
        return facade.aceitarSolicitacao(id);
    }

    public SolicitacaoCarona cancelar(String id) throws IOException {
        return facade.cancelarSolicitacao(id);
    }

    public SolicitacaoCarona atualizar(String id, String passageiroId, String caronaId, String status) throws IOException {
        return service.atualizar(id, passageiroId, caronaId, status);
    }

    public SolicitacaoCarona recusar(String id) throws IOException {
        return facade.recusarSolicitacao(id);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }
}

