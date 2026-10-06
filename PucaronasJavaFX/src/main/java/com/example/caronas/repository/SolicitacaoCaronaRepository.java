package com.example.caronas.repository;

import com.example.caronas.model.SolicitacaoCarona;
import java.util.List;
import java.util.Optional;

public class SolicitacaoCaronaRepository extends BaseRepository<SolicitacaoCarona> {
    
    public SolicitacaoCaronaRepository() {
        super("solicitacoes_carona");
    }

    @Override
    protected String idDe(SolicitacaoCarona entidade) {
        return entidade.getId();
    }

    public List<SolicitacaoCarona> buscarPorPassageiro(String passageiroId) {
        List<SolicitacaoCarona> solicitacoes = listar();
        return solicitacoes.stream()
                .filter(s -> s.getPassageiroId().equals(passageiroId))
                .toList();
    }

    public List<SolicitacaoCarona> buscarPorCarona(String caronaId) {
        List<SolicitacaoCarona> solicitacoes = listar();
        return solicitacoes.stream()
                .filter(s -> s.getCaronaId().equals(caronaId))
                .toList();
    }

    public List<SolicitacaoCarona> buscarPorStatus(String status) {
        List<SolicitacaoCarona> solicitacoes = listar();
        return solicitacoes.stream()
                .filter(s -> s.getStatus().equals(status))
                .toList();
    }

}
