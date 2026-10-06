package com.example.caronas.service;

import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.repository.SolicitacaoCaronaRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SolicitacaoCaronaService {
    private SolicitacaoCaronaRepository repository;

    public SolicitacaoCaronaService() {
        this.repository = new SolicitacaoCaronaRepository();
    }

    public SolicitacaoCarona criar(String passageiroId, String caronaId) throws IOException {
        if (passageiroId == null || passageiroId.isEmpty() || caronaId == null || caronaId.isEmpty()) {
            throw new IllegalArgumentException("Passageiro e carona são obrigatórios");
        }

        SolicitacaoCarona solicitacao = new SolicitacaoCarona(
                UUID.randomUUID().toString(),
                passageiroId,
                caronaId,
                "pendente",
                LocalDateTime.now()
        );
        repository.inserir(solicitacao);
        return solicitacao;
    }

    public List<SolicitacaoCarona> listar() {
        return repository.listar();
    }

    public Optional<SolicitacaoCarona> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public List<SolicitacaoCarona> buscarPorPassageiro(String passageiroId) {
        return repository.buscarPorPassageiro(passageiroId);
    }

    public List<SolicitacaoCarona> buscarPorCarona(String caronaId) {
        return repository.buscarPorCarona(caronaId);
    }

    public List<SolicitacaoCarona> buscarPorStatus(String status) {
        return repository.buscarPorStatus(status);
    }

    public SolicitacaoCarona aceitar(String id) throws IOException {
        Optional<SolicitacaoCarona> solicitacao = repository.buscarPorId(id);
        if (solicitacao.isEmpty()) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }
        solicitacao.get().aceitar();   // State: lança TransicaoInvalidaException se não for permitido
        repository.atualizar(solicitacao.get());
        return solicitacao.get();
    }

    public SolicitacaoCarona cancelar(String id) throws IOException {
        Optional<SolicitacaoCarona> solicitacao = repository.buscarPorId(id);
        if (solicitacao.isEmpty()) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }
        solicitacao.get().cancelar();  // State
        repository.atualizar(solicitacao.get());
        return solicitacao.get();
    }

    /**
     * Atualiza passageiro/carona. Mudança de status NÃO é atribuição direta: passa pelas
     * transições do State, então "recusada → aceita", por exemplo, é rejeitada.
     */
    public SolicitacaoCarona atualizar(String id, String passageiroId, String caronaId, String status) throws IOException {
        Optional<SolicitacaoCarona> solicitacao = repository.buscarPorId(id);
        if (solicitacao.isEmpty()) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }
        SolicitacaoCarona s = solicitacao.get();
        s.setPassageiroId(passageiroId);
        s.setCaronaId(caronaId);
        if (status != null && !status.equals(s.getStatus())) {
            switch (status) {
                case "aceita" -> s.aceitar();
                case "recusada" -> s.recusar();
                case "cancelada" -> s.cancelar();
                default -> throw new com.example.caronas.state.TransicaoInvalidaException(s.getStatus(), "voltar para '" + status + "' a partir de");
            }
        }
        repository.atualizar(s);
        return s;
    }

    public SolicitacaoCarona recusar(String id) throws IOException {
        Optional<SolicitacaoCarona> solicitacao = repository.buscarPorId(id);
        if (solicitacao.isEmpty()) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }
        solicitacao.get().recusar();   // State
        repository.atualizar(solicitacao.get());
        return solicitacao.get();
    }

    public void excluir(String id) throws IOException {
        Optional<SolicitacaoCarona> solicitacao = repository.buscarPorId(id);
        if (solicitacao.isEmpty()) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }
        repository.excluirPorId(id);
    }
}

