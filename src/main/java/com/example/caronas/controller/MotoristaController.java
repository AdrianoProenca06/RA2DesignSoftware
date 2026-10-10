package com.example.caronas.controller;

import com.example.caronas.model.Motorista;
import com.example.caronas.service.MotoristaService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MotoristaController {
    private MotoristaService service;

    public MotoristaController() {
        this.service = new MotoristaService();
    }

    public Motorista criar(String usuarioId, String veiculoId, String cnh) throws IOException {
        return service.criar(usuarioId, veiculoId, cnh);
    }

    public List<Motorista> listar() {
        return service.listar();
    }

    public Optional<Motorista> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public Optional<Motorista> buscarPorUsuarioId(String usuarioId) {
        return service.buscarPorUsuarioId(usuarioId);
    }

    public List<Motorista> buscarPorStatus(String status) {
        return service.buscarPorStatus(status);
    }

    public Motorista atualizar(String id, String usuarioId, String veiculoId, String cnh, String status, double avaliacao) throws IOException {
        return service.atualizar(id, usuarioId, veiculoId, cnh, status, avaliacao);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }

    public void ativar(String id) throws IOException {
        service.ativar(id);
    }

    public void inativar(String id) throws IOException {
        service.inativar(id);
    }
}

