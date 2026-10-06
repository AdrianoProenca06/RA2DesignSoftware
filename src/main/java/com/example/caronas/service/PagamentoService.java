package com.example.caronas.service;

import com.example.caronas.model.Pagamento;
import com.example.caronas.repository.PagamentoRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PagamentoService {
    private PagamentoRepository repository;

    public PagamentoService() {
        this.repository = new PagamentoRepository();
    }

    public Pagamento criar(String solicitacaoCaronaId, double valor, String metodo) throws IOException {
        if (solicitacaoCaronaId == null || solicitacaoCaronaId.isEmpty()) {
            throw new IllegalArgumentException("Solicitação de carona é obrigatória");
        }

        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        if (!metodo.equals("pix") && !metodo.equals("dinheiro")) {
            throw new IllegalArgumentException("Método deve ser 'pix' ou 'dinheiro'");
        }

        Pagamento pagamento = new Pagamento(
                UUID.randomUUID().toString(),
                solicitacaoCaronaId,
                valor,
                "pendente",
                metodo,
                null
        );
        repository.inserir(pagamento);
        return pagamento;
    }

    public List<Pagamento> listar() {
        return repository.listar();
    }

    public Optional<Pagamento> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public List<Pagamento> buscarPorSolicitacao(String solicitacaoCaronaId) {
        return repository.buscarPorSolicitacao(solicitacaoCaronaId);
    }

    public List<Pagamento> buscarPorStatus(String status) {
        return repository.buscarPorStatus(status);
    }

    public List<Pagamento> buscarPorMetodo(String metodo) {
        return repository.buscarPorMetodo(metodo);
    }

    public Pagamento realizarPagamento(String id) throws IOException {
        Optional<Pagamento> pagamento = repository.buscarPorId(id);
        if (pagamento.isEmpty()) {
            throw new IllegalArgumentException("Pagamento não encontrado");
        }
        pagamento.get().setStatus("pago");
        pagamento.get().setDataPagamento(LocalDateTime.now());
        repository.atualizar(pagamento.get());
        return pagamento.get();
    }

    public Pagamento atualizar(String id, String solicitacaoCaronaId, double valor, String metodo, String status) throws IOException {
        Optional<Pagamento> pagamento = repository.buscarPorId(id);
        if (pagamento.isEmpty()) {
            throw new IllegalArgumentException("Pagamento não encontrado");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
        if (!metodo.equals("pix") && !metodo.equals("dinheiro")) {
            throw new IllegalArgumentException("Método deve ser 'pix' ou 'dinheiro'");
        }
        pagamento.get().setSolicitacaoCaronaId(solicitacaoCaronaId);
        pagamento.get().setValor(valor);
        pagamento.get().setMetodo(metodo);
        pagamento.get().setStatus(status);
        repository.atualizar(pagamento.get());
        return pagamento.get();
    }

    public Pagamento cancelarPagamento(String id) throws IOException {
        Optional<Pagamento> pagamento = repository.buscarPorId(id);
        if (pagamento.isEmpty()) {
            throw new IllegalArgumentException("Pagamento não encontrado");
        }
        pagamento.get().setStatus("cancelado");
        repository.atualizar(pagamento.get());
        return pagamento.get();
    }

    public void excluir(String id) throws IOException {
        Optional<Pagamento> pagamento = repository.buscarPorId(id);
        if (pagamento.isEmpty()) {
            throw new IllegalArgumentException("Pagamento não encontrado");
        }
        repository.excluirPorId(id);
    }
}

