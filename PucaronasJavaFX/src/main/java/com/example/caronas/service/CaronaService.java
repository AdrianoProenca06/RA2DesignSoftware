package com.example.caronas.service;

import com.example.caronas.model.Carona;
import com.example.caronas.repository.CaronaRepository;
import com.example.caronas.strategy.EstrategiasPreco;
import com.example.caronas.strategy.PrecoFixo;
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
        return criar(motoristaId, origem, destino, horario, vagasDisponiveis, 10.0, 10.0, PrecoFixo.NOME);
    }

    public Carona criar(String motoristaId, String origem, String destino, LocalDateTime horario, int vagasDisponiveis,
                        double distanciaKm, double valorBase, String politicaPreco) throws IOException {
        if (motoristaId == null || motoristaId.isEmpty() || origem == null || origem.isEmpty() || destino == null || destino.isEmpty()) {
            throw new IllegalArgumentException("Motorista, origem e destino são obrigatórios");
        }

        if (vagasDisponiveis <= 0) {
            throw new IllegalArgumentException("Número de vagas deve ser maior que zero");
        }

        validarPreco(distanciaKm, valorBase, politicaPreco);

        Carona carona = new Carona(UUID.randomUUID().toString(), motoristaId, origem, destino, horario,
                vagasDisponiveis, distanciaKm, valorBase, politicaPreco);
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
        Carona atual = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada"));
        return atualizar(id, motoristaId, origem, destino, horario, vagasDisponiveis,
                atual.getDistanciaKm(), atual.getValorBase(), atual.getPoliticaPreco());
    }

    public Carona atualizar(String id, String motoristaId, String origem, String destino, LocalDateTime horario, int vagasDisponiveis,
                            double distanciaKm, double valorBase, String politicaPreco) throws IOException {
        validarPreco(distanciaKm, valorBase, politicaPreco);
        Optional<Carona> carona = repository.buscarPorId(id);
        if (carona.isEmpty()) {
            throw new IllegalArgumentException("Carona não encontrada");
        }

        carona.get().setMotoristaId(motoristaId);
        carona.get().setOrigem(origem);
        carona.get().setDestino(destino);
        carona.get().setHorario(horario);
        carona.get().setVagasDisponiveis(vagasDisponiveis);
        carona.get().setDistanciaKm(distanciaKm);
        carona.get().setValorBase(valorBase);
        carona.get().setPoliticaPreco(politicaPreco);

        repository.atualizar(carona.get());
        return carona.get();
    }

    private void validarPreco(double distanciaKm, double valorBase, String politicaPreco) {
        if (distanciaKm <= 0 || valorBase <= 0) {
            throw new IllegalArgumentException("Distância e valor base devem ser maiores que zero");
        }
        EstrategiasPreco.por(politicaPreco); // lança IllegalArgumentException se a política não existir
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

