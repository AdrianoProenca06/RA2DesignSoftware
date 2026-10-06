package com.example.caronas.controller;

import com.example.caronas.model.Carona;
import com.example.caronas.service.CaronaService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CaronaController {
    private CaronaService service;

    public CaronaController() {
        this.service = new CaronaService();
    }

    public Carona criar(String motoristaId, String origem, String destino, LocalDateTime horario, int vagasDisponiveis) throws IOException {
        return service.criar(motoristaId, origem, destino, horario, vagasDisponiveis);
    }

    public List<Carona> listar() {
        return service.listar();
    }

    public Optional<Carona> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public List<Carona> buscarPorMotorista(String motoristaId) {
        return service.buscarPorMotorista(motoristaId);
    }

    public List<Carona> buscarPorRota(String origem, String destino) {
        return service.buscarPorRota(origem, destino);
    }

    public List<Carona> buscarComVagasDisponiveis() {
        return service.buscarComVagasDisponiveis();
    }

    public Carona atualizar(String id, String motoristaId, String origem, String destino, LocalDateTime horario, int vagasDisponiveis) throws IOException {
        return service.atualizar(id, motoristaId, origem, destino, horario, vagasDisponiveis);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }

    public void reduzirVagas(String caronaId) throws IOException {
        service.reduzirVagas(caronaId);
    }

    public void aumentarVagas(String caronaId) throws IOException {
        service.aumentarVagas(caronaId);
    }
}

