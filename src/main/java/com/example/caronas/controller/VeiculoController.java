package com.example.caronas.controller;

import com.example.caronas.model.Veiculo;
import com.example.caronas.service.VeiculoService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VeiculoController {
    private VeiculoService service;

    public VeiculoController() {
        this.service = new VeiculoService();
    }

    public Veiculo criar(String modelo, String placa, String cor, int capacidade) throws IOException {
        return service.criar(modelo, placa, cor, capacidade);
    }

    public List<Veiculo> listar() {
        return service.listar();
    }

    public Optional<Veiculo> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return service.buscarPorPlaca(placa);
    }

    public List<Veiculo> buscarPorModelo(String modelo) {
        return service.buscarPorModelo(modelo);
    }

    public Veiculo atualizar(String id, String modelo, String placa, String cor, int capacidade) throws IOException {
        return service.atualizar(id, modelo, placa, cor, capacidade);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }
}

