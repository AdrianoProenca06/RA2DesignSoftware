package com.example.caronas.repository;

import com.example.caronas.model.SolicitacaoCarona;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SolicitacaoCaronaRepository extends BaseRepository<SolicitacaoCarona> {
    
    public SolicitacaoCaronaRepository() {
        super("solicitacoes_carona");
    }

    public Optional<SolicitacaoCarona> buscarPorId(String id) {
        List<SolicitacaoCarona> solicitacoes = listar();
        return solicitacoes.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
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

    @Override
    public void atualizar(SolicitacaoCarona solicitacao) throws IOException {
        List<SolicitacaoCarona> solicitacoes = listar();
        for (int i = 0; i < solicitacoes.size(); i++) {
            if (solicitacoes.get(i).getId().equals(solicitacao.getId())) {
                solicitacoes.set(i, solicitacao);
                break;
            }
        }
        salvarLista(solicitacoes);
    }

    @Override
    public void excluir(SolicitacaoCarona solicitacao) throws IOException {
        List<SolicitacaoCarona> solicitacoes = listar();
        solicitacoes.removeIf(s -> s.getId().equals(solicitacao.getId()));
        salvarLista(solicitacoes);
    }

    public void excluirPorId(String id) throws IOException {
        List<SolicitacaoCarona> solicitacoes = listar();
        solicitacoes.removeIf(s -> s.getId().equals(id));
        salvarLista(solicitacoes);
    }
}

