package com.example.caronas.repository;

import com.example.caronas.model.Carona;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CaronaRepository extends BaseRepository<Carona> {
    
    public CaronaRepository() {
        super("caronas");
    }

    public Optional<Carona> buscarPorId(String id) {
        List<Carona> caronas = listar();
        return caronas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public List<Carona> buscarPorMotorista(String motoristaId) {
        List<Carona> caronas = listar();
        return caronas.stream()
                .filter(c -> c.getMotoristaId().equals(motoristaId))
                .toList();
    }

    public List<Carona> buscarPorRota(String origem, String destino) {
        List<Carona> caronas = listar();
        return caronas.stream()
                .filter(c -> c.getOrigem().equalsIgnoreCase(origem) && c.getDestino().equalsIgnoreCase(destino))
                .toList();
    }

    public List<Carona> buscarComVagasDisponiveis() {
        List<Carona> caronas = listar();
        return caronas.stream()
                .filter(c -> c.getVagasDisponiveis() > 0)
                .toList();
    }

    @Override
    public void atualizar(Carona carona) throws IOException {
        List<Carona> caronas = listar();
        for (int i = 0; i < caronas.size(); i++) {
            if (caronas.get(i).getId().equals(carona.getId())) {
                caronas.set(i, carona);
                break;
            }
        }
        salvarLista(caronas);
    }

    @Override
    public void excluir(Carona carona) throws IOException {
        List<Carona> caronas = listar();
        caronas.removeIf(c -> c.getId().equals(carona.getId()));
        salvarLista(caronas);
    }

    public void excluirPorId(String id) throws IOException {
        List<Carona> caronas = listar();
        caronas.removeIf(c -> c.getId().equals(id));
        salvarLista(caronas);
    }
}

