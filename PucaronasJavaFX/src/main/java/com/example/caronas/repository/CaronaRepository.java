package com.example.caronas.repository;

import com.example.caronas.model.Carona;
import java.util.List;
import java.util.Optional;

public class CaronaRepository extends BaseRepository<Carona> {
    
    public CaronaRepository() {
        super("caronas");
    }

    @Override
    protected String idDe(Carona entidade) {
        return entidade.getId();
    }

    /** Gancho do Template Method: invariante do domínio, vale para inserir E atualizar. */
    @Override
    protected void validar(Carona carona) {
        if (carona.getVagasDisponiveis() < 0) {
            throw new IllegalArgumentException("Vagas disponíveis não pode ser negativo");
        }
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

}
