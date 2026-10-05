package com.example.caronas.service;

import com.example.caronas.model.Veiculo;
import com.example.caronas.repository.VeiculoRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VeiculoService {
    private VeiculoRepository repository;

    public VeiculoService() {
        this.repository = new VeiculoRepository();
    }

    public Veiculo criar(String modelo, String placa, String cor, int capacidade) throws IOException {
        if (modelo == null || modelo.isEmpty() || placa == null || placa.isEmpty()) {
            throw new IllegalArgumentException("Modelo e placa são obrigatórios");
        }

        if (capacidade <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser maior que zero");
        }

        Optional<Veiculo> existente = repository.buscarPorPlaca(placa);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Placa já cadastrada");
        }

        Veiculo veiculo = new Veiculo(UUID.randomUUID().toString(), modelo, placa.toUpperCase(), cor, capacidade);
        repository.inserir(veiculo);
        return veiculo;
    }

    public List<Veiculo> listar() {
        return repository.listar();
    }

    public Optional<Veiculo> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return repository.buscarPorPlaca(placa);
    }

    public List<Veiculo> buscarPorModelo(String modelo) {
        return repository.buscarPorModelo(modelo);
    }

    public Veiculo atualizar(String id, String modelo, String placa, String cor, int capacidade) throws IOException {
        Optional<Veiculo> veiculo = repository.buscarPorId(id);
        if (veiculo.isEmpty()) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }

        veiculo.get().setModelo(modelo);
        veiculo.get().setPlaca(placa.toUpperCase());
        veiculo.get().setCor(cor);
        veiculo.get().setCapacidade(capacidade);

        repository.atualizar(veiculo.get());
        return veiculo.get();
    }

    public void excluir(String id) throws IOException {
        Optional<Veiculo> veiculo = repository.buscarPorId(id);
        if (veiculo.isEmpty()) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }
        repository.excluirPorId(id);
    }
}

