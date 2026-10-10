package com.example.caronas.service;

import com.example.caronas.model.Motorista;
import com.example.caronas.repository.MotoristaRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MotoristaService {
    private MotoristaRepository repository;

    public MotoristaService() {
        this.repository = new MotoristaRepository();
    }

    public Motorista criar(String usuarioId, String veiculoId, String cnh) throws IOException {
        if (usuarioId == null || usuarioId.isEmpty() || veiculoId == null || veiculoId.isEmpty() || cnh == null || cnh.isEmpty()) {
            throw new IllegalArgumentException("Usuário, veículo e CNH são obrigatórios");
        }

        Motorista motorista = new Motorista(UUID.randomUUID().toString(), usuarioId, veiculoId, cnh, "ativo", 0.0);
        repository.inserir(motorista);
        return motorista;
    }

    public List<Motorista> listar() {
        return repository.listar();
    }

    public Optional<Motorista> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public Optional<Motorista> buscarPorUsuarioId(String usuarioId) {
        return repository.buscarPorUsuarioId(usuarioId);
    }

    public List<Motorista> buscarPorStatus(String status) {
        return repository.buscarPorStatus(status);
    }

    public Motorista atualizar(String id, String usuarioId, String veiculoId, String cnh, String status, double avaliacao) throws IOException {
        Optional<Motorista> motorista = repository.buscarPorId(id);
        if (motorista.isEmpty()) {
            throw new IllegalArgumentException("Motorista não encontrado");
        }

        motorista.get().setUsuarioId(usuarioId);
        motorista.get().setVeiculoId(veiculoId);
        motorista.get().setCnh(cnh);
        motorista.get().setStatus(status);
        motorista.get().setAvaliacao(avaliacao);

        repository.atualizar(motorista.get());
        return motorista.get();
    }

    public void excluir(String id) throws IOException {
        Optional<Motorista> motorista = repository.buscarPorId(id);
        if (motorista.isEmpty()) {
            throw new IllegalArgumentException("Motorista não encontrado");
        }
        repository.excluirPorId(id);
    }

    public void ativar(String id) throws IOException {
        Optional<Motorista> motorista = repository.buscarPorId(id);
        if (motorista.isEmpty()) {
            throw new IllegalArgumentException("Motorista não encontrado");
        }
        motorista.get().setStatus("ativo");
        repository.atualizar(motorista.get());
    }

    public void inativar(String id) throws IOException {
        Optional<Motorista> motorista = repository.buscarPorId(id);
        if (motorista.isEmpty()) {
            throw new IllegalArgumentException("Motorista não encontrado");
        }
        motorista.get().setStatus("inativo");
        repository.atualizar(motorista.get());
    }
}

