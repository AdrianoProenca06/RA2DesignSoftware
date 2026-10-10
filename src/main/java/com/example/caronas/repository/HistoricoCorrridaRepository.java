package com.example.caronas.repository;

import com.example.caronas.model.HistoricoCorrida;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HistoricoCorrridaRepository extends BaseRepository<HistoricoCorrida> {
    
    public HistoricoCorrridaRepository() {
        super("historico_corridas");
    }

    public Optional<HistoricoCorrida> buscarPorId(String id) {
        List<HistoricoCorrida> historicos = listar();
        return historicos.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst();
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

    @Override
    public void atualizar(HistoricoCorrida historico) throws IOException {
        List<HistoricoCorrida> historicos = listar();
        for (int i = 0; i < historicos.size(); i++) {
            if (historicos.get(i).getId().equals(historico.getId())) {
                historicos.set(i, historico);
                break;
            }
        }
        salvarLista(historicos);
    }

    @Override
    public void excluir(HistoricoCorrida historico) throws IOException {
        List<HistoricoCorrida> historicos = listar();
        historicos.removeIf(h -> h.getId().equals(historico.getId()));
        salvarLista(historicos);
    }

    public void excluirPorId(String id) throws IOException {
        List<HistoricoCorrida> historicos = listar();
        historicos.removeIf(h -> h.getId().equals(id));
        salvarLista(historicos);
    }
}

