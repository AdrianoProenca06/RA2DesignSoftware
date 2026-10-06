package com.example.caronas.repository;

import com.example.caronas.model.Pagamento;
import java.util.List;
import java.util.Optional;

public class PagamentoRepository extends BaseRepository<Pagamento> {
    
    public PagamentoRepository() {
        super("pagamentos");
    }

    @Override
    protected String idDe(Pagamento entidade) {
        return entidade.getId();
    }

    public List<Pagamento> buscarPorSolicitacao(String solicitacaoCaronaId) {
        List<Pagamento> pagamentos = listar();
        return pagamentos.stream()
                .filter(p -> p.getSolicitacaoCaronaId().equals(solicitacaoCaronaId))
                .toList();
    }

    public List<Pagamento> buscarPorStatus(String status) {
        List<Pagamento> pagamentos = listar();
        return pagamentos.stream()
                .filter(p -> p.getStatus().equals(status))
                .toList();
    }

    public List<Pagamento> buscarPorMetodo(String metodo) {
        List<Pagamento> pagamentos = listar();
        return pagamentos.stream()
                .filter(p -> p.getMetodo().equals(metodo))
                .toList();
    }

}
