package com.example.caronas.controller;

import com.example.caronas.model.HistoricoCorrida;
import com.example.caronas.service.HistoricoCorrridaService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HistoricoCorrridaController {
    private HistoricoCorrridaService service;

    public HistoricoCorrridaController() {
        this.service = new HistoricoCorrridaService();
    }

    public HistoricoCorrida criar(String motoristaId, String passageiroId, String trajeto, double valor) throws IOException {
        return service.criar(motoristaId, passageiroId, trajeto, valor);
    }

    public List<HistoricoCorrida> listar() {
        return service.listar();
    }

    public Optional<HistoricoCorrida> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public List<HistoricoCorrida> buscarPorMotorista(String motoristaId) {
        return service.buscarPorMotorista(motoristaId);
    }

    public List<HistoricoCorrida> buscarPorPassageiro(String passageiroId) {
        return service.buscarPorPassageiro(passageiroId);
    }

    public double calcularRendimentoMotorista(String motoristaId) {
        return service.calcularRendimentoMotorista(motoristaId);
    }

    public HistoricoCorrida atualizar(String id, String motoristaId, String passageiroId, String trajeto, double valor) throws IOException {
        return service.atualizar(id, motoristaId, passageiroId, trajeto, valor);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }
}

