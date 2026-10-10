package com.example.caronas.service;

import com.example.caronas.model.HistoricoCorrida;
import com.example.caronas.repository.HistoricoCorrridaRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HistoricoCorrridaService {
    private HistoricoCorrridaRepository repository;

    public HistoricoCorrridaService() {
        this.repository = new HistoricoCorrridaRepository();
    }

    public HistoricoCorrida criar(String motoristaId, String passageiroId, String trajeto, double valor) throws IOException {
        if (motoristaId == null || motoristaId.isEmpty() || passageiroId == null || passageiroId.isEmpty() || trajeto == null || trajeto.isEmpty()) {
            throw new IllegalArgumentException("Motorista, passageiro e trajeto são obrigatórios");
        }

        HistoricoCorrida historico = new HistoricoCorrida(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                motoristaId,
                passageiroId,
                trajeto,
                valor
        );
        repository.inserir(historico);
        return historico;
    }

    public List<HistoricoCorrida> listar() {
        return repository.listar();
    }

    public Optional<HistoricoCorrida> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public List<HistoricoCorrida> buscarPorMotorista(String motoristaId) {
        return repository.buscarPorMotorista(motoristaId);
    }

    public List<HistoricoCorrida> buscarPorPassageiro(String passageiroId) {
        return repository.buscarPorPassageiro(passageiroId);
    }

    public double calcularRendimentoMotorista(String motoristaId) {
        return repository.calcularRendimentoMotorista(motoristaId);
    }

    public HistoricoCorrida atualizar(String id, String motoristaId, String passageiroId, String trajeto, double valor) throws IOException {
        Optional<HistoricoCorrida> historico = repository.buscarPorId(id);
        if (historico.isEmpty()) {
            throw new IllegalArgumentException("Histórico não encontrado");
        }

        historico.get().setMotoristaId(motoristaId);
        historico.get().setPassageiroId(passageiroId);
        historico.get().setTrajeto(trajeto);
        historico.get().setValor(valor);

        repository.atualizar(historico.get());
        return historico.get();
    }

    public void excluir(String id) throws IOException {
        Optional<HistoricoCorrida> historico = repository.buscarPorId(id);
        if (historico.isEmpty()) {
            throw new IllegalArgumentException("Histórico não encontrado");
        }
        repository.excluirPorId(id);
    }
}

