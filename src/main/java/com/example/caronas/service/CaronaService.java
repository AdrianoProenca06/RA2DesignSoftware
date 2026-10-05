package com.example.caronas.service;

import com.example.caronas.model.Carona;
import com.example.caronas.repository.CaronaRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CaronaService {
    private CaronaRepository repository;

    public CaronaService() {
        this.repository = new CaronaRepository();
    }

    public Carona criar(String motoristaId, String origem, String destino, LocalDateTime horario, int vagasDisponiveis) throws IOException {
        if (motoristaId == null || motoristaId.isEmpty() || origem == null || origem.isEmpty() || destino == null || destino.isEmpty()) {
            throw new IllegalArgumentException("Motorista, origem e destino são obrigatórios");
        }

        if (vagasDisponiveis <= 0) {
            throw new IllegalArgumentException("Número de vagas deve ser maior que zero");
        }

        Carona carona = new Carona(UUID.randomUUID().toString(), motoristaId, origem, destino, horario, vagasDisponiveis);
        repository.inserir(carona);
        return carona;
    }

    public List<Carona> listar() {
        return repository.listar();
    }

    public Optional<Carona> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public List<Carona> buscarPorMotorista(String motoristaId) {
        return repository.buscarPorMotorista(motoristaId);
    }

    public List<Carona> buscarPorRota(String origem, String destino) {
        return repository.buscarPorRota(origem, destino);
    }

    public List<Carona> buscarComVagasDisponiveis() {
        return repository.buscarComVagasDisponiveis();
    }

    public Carona atualizar(String id, String motoristaId, String origem, String destino, LocalDateTime horario, int vagasDisponiveis) throws IOException {
        Optional<Carona> carona = repository.buscarPorId(id);
        if (carona.isEmpty()) {
            throw new IllegalArgumentException("Carona não encontrada");
        }

        carona.get().setMotoristaId(motoristaId);
        carona.get().setOrigem(origem);
        carona.get().setDestino(destino);
        carona.get().setHorario(horario);
        carona.get().setVagasDisponiveis(vagasDisponiveis);

        repository.atualizar(carona.get());
        return carona.get();
    }

    public void excluir(String id) throws IOException {
        Optional<Carona> carona = repository.buscarPorId(id);
        if (carona.isEmpty()) {
            throw new IllegalArgumentException("Carona não encontrada");
        }
        repository.excluirPorId(id);
    }

    public void reduzirVagas(String caronaId) throws IOException {
        Optional<Carona> carona = repository.buscarPorId(caronaId);
        if (carona.isEmpty()) {
            throw new IllegalArgumentException("Carona não encontrada");
        }
        if (carona.get().getVagasDisponiveis() > 0) {
            carona.get().setVagasDisponiveis(carona.get().getVagasDisponiveis() - 1);
            repository.atualizar(carona.get());
        }
    }

    public void aumentarVagas(String caronaId) throws IOException {
        Optional<Carona> carona = repository.buscarPorId(caronaId);
        if (carona.isEmpty()) {
            throw new IllegalArgumentException("Carona não encontrada");
        }
        carona.get().setVagasDisponiveis(carona.get().getVagasDisponiveis() + 1);
        repository.atualizar(carona.get());
    }
}

