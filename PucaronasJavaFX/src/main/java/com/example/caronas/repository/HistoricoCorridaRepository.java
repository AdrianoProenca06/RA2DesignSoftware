package com.example.caronas.repository;

import com.example.caronas.model.HistoricoCorrida;
import java.util.List;
import java.util.Optional;

public class HistoricoCorrridaRepository extends BaseRepository<HistoricoCorrida> {
    
    public HistoricoCorrridaRepository() {
        super("historico_corridas");
    }

    @Override
    protected String idDe(HistoricoCorrida entidade) {
        return entidade.getId();
    }

    public List<HistoricoCorrida> buscarPorMotorista(String motoristaId) {
        List<HistoricoCorrida> historicos = listar();
        return historicos.stream()
                .filter(h -> h.getMotoristaId().equals(motoristaId))
                .toList();
    }

    public List<HistoricoCorrida> buscarPorPassageiro(String passageiroId) {
        List<HistoricoCorrida> historicos = listar();
        return historicos.stream()
                .filter(h -> h.getPassageiroId().equals(passageiroId))
                .toList();
    }

    public double calcularRendimentoMotorista(String motoristaId) {
        List<HistoricoCorrida> historicos = listar();
        return historicos.stream()
                .filter(h -> h.getMotoristaId().equals(motoristaId))
                .mapToDouble(HistoricoCorrida::getValor)
                .sum();
    }

}
