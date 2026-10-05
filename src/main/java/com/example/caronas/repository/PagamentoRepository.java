package com.example.caronas.repository;

import com.example.caronas.model.Pagamento;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PagamentoRepository extends BaseRepository<Pagamento> {
    
    public PagamentoRepository() {
        super("pagamentos");
    }

    public Optional<Pagamento> buscarPorId(String id) {
        List<Pagamento> pagamentos = listar();
        return pagamentos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
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

    @Override
    public void atualizar(Pagamento pagamento) throws IOException {
        List<Pagamento> pagamentos = listar();
        for (int i = 0; i < pagamentos.size(); i++) {
            if (pagamentos.get(i).getId().equals(pagamento.getId())) {
                pagamentos.set(i, pagamento);
                break;
            }
        }
        salvarLista(pagamentos);
    }

    @Override
    public void excluir(Pagamento pagamento) throws IOException {
        List<Pagamento> pagamentos = listar();
        pagamentos.removeIf(p -> p.getId().equals(pagamento.getId()));
        salvarLista(pagamentos);
    }

    public void excluirPorId(String id) throws IOException {
        List<Pagamento> pagamentos = listar();
        pagamentos.removeIf(p -> p.getId().equals(id));
        salvarLista(pagamentos);
    }
}

