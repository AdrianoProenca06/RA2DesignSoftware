package com.example.caronas.facade;

import com.example.caronas.event.*;
import com.example.caronas.model.*;
import com.example.caronas.repository.*;
import com.example.caronas.service.NotificacaoService;
import com.example.caronas.strategy.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CaronasFacade {
    private final CaronaRepository caronas = new CaronaRepository();
    private final SolicitacaoCaronaRepository solicitacoes = new SolicitacaoCaronaRepository();
    private final PagamentoRepository pagamentos = new PagamentoRepository();
    private final HistoricoCorrridaRepository historicos = new HistoricoCorrridaRepository();
    private final EventBus eventos = EventBus.getInstance();

    private static final NotificacaoObserver OBSERVADOR_NOTIFICACOES =
            new NotificacaoObserver(new NotificacaoService());

    public CaronasFacade() {
        // O EventBus é Singleton: não registrar um novo observador a cada fachada.
        eventos.registrar(OBSERVADOR_NOTIFICACOES);
    }

    public SolicitacaoCarona solicitar(String caronaId, String passageiroId) throws IOException {
        Carona c = caronas.buscarPorId(caronaId).orElseThrow(() -> new IllegalArgumentException("Carona não encontrada"));
        if (c.getVagasDisponiveis() <= 0) throw new IllegalStateException("Carona sem vagas");
        if (c.getMotoristaId().equals(passageiroId)) throw new IllegalArgumentException("Motorista não pode solicitar a própria carona");
        boolean duplicada = solicitacoes.buscarPorCarona(caronaId).stream().anyMatch(s -> s.getPassageiroId().equals(passageiroId) && (s.getStatus().equals("pendente") || s.getStatus().equals("aceita")));
        if (duplicada) throw new IllegalStateException("Já existe solicitação ativa para este passageiro");
        SolicitacaoCarona s = new SolicitacaoCarona(UUID.randomUUID().toString(), passageiroId, caronaId, "pendente", LocalDateTime.now());
        solicitacoes.inserir(s);
        eventos.publicar(new EventoCarona(EventoCarona.Tipo.SOLICITACAO_CRIADA, c.getMotoristaId(), "Nova solicitação para a carona " + c.getOrigem() + " → " + c.getDestino()));
        return s;
    }

    public Pagamento aceitar(String solicitacaoId, String metodo) throws IOException {
        SolicitacaoCarona s = buscarSolicitacao(solicitacaoId);
        Carona c = buscarCarona(s.getCaronaId());
        // Validar estado e método de pagamento antes de modificar a ocupação.
        if (!"pendente".equals(s.getStatus()))
            throw new IllegalStateException("Somente solicitação pendente pode ser aceita");
        String metodoNormalizado = normalizarMetodo(metodo);
        if (c.getVagasDisponiveis() <= 0)
            throw new IllegalStateException("Carona sem vagas");
        s.aceitar();
        c.ocuparVaga();
        int confirmados = (int) solicitacoes.buscarPorCarona(c.getId()).stream().filter(x -> x.getStatus().equals("aceita")).count();
        PoliticaPreco politica = PoliticaPrecoFactory.criar(c.getPoliticaPreco());
        double valor = politica.calcular(c.getValorBase(), c.getDistanciaKm(), Math.max(0, confirmados - 1));
        Pagamento p = new Pagamento(UUID.randomUUID().toString(), s.getId(), valor, "pendente", metodoNormalizado, null);
        caronas.atualizar(c); solicitacoes.atualizar(s); pagamentos.inserir(p);
        eventos.publicar(new EventoCarona(EventoCarona.Tipo.SOLICITACAO_ACEITA, s.getPassageiroId(), "Sua solicitação foi aceita. Valor: R$ " + String.format("%.2f", valor)));
        return p;
    }

    public void recusar(String solicitacaoId) throws IOException {
        SolicitacaoCarona s = buscarSolicitacao(solicitacaoId); s.recusar(); solicitacoes.atualizar(s);
        eventos.publicar(new EventoCarona(EventoCarona.Tipo.SOLICITACAO_RECUSADA, s.getPassageiroId(), "Sua solicitação de carona foi recusada."));
    }

    public void cancelar(String solicitacaoId) throws IOException {
        SolicitacaoCarona s = buscarSolicitacao(solicitacaoId);
        Pagamento p = pagamentos.buscarPorSolicitacao(s.getId()).stream().findFirst().orElse(null);
        if (p != null && "pago".equals(p.getStatus())) throw new IllegalStateException("Solicitação já paga não pode ser cancelada");
        boolean eraAceita = "aceita".equals(s.getStatus()); s.cancelar(); solicitacoes.atualizar(s);
        Carona c = buscarCarona(s.getCaronaId());
        if (eraAceita) { c.devolverVaga(); caronas.atualizar(c); if (p != null) { p.setStatus("cancelado"); pagamentos.atualizar(p); } }
        eventos.publicar(new EventoCarona(EventoCarona.Tipo.SOLICITACAO_CANCELADA, c.getMotoristaId(), "Uma solicitação da carona foi cancelada."));
    }

    public Pagamento pagar(String solicitacaoId) throws IOException {
        SolicitacaoCarona s = buscarSolicitacao(solicitacaoId);
        if (!"aceita".equals(s.getStatus())) throw new IllegalStateException("Somente solicitação aceita pode ser paga");
        Pagamento p = pagamentos.buscarPorSolicitacao(s.getId()).stream().findFirst().orElseThrow(() -> new IllegalStateException("Pagamento não encontrado"));
        if (!"pendente".equals(p.getStatus())) throw new IllegalStateException("Pagamento já processado");
        p.setStatus("pago"); p.setDataPagamento(LocalDateTime.now()); pagamentos.atualizar(p);
        Carona c = buscarCarona(s.getCaronaId());
        historicos.inserir(new HistoricoCorrida(UUID.randomUUID().toString(), LocalDateTime.now(), c.getMotoristaId(), s.getPassageiroId(), c.getOrigem()+" → "+c.getDestino(), p.getValor()));
        eventos.publicar(new EventoCarona(EventoCarona.Tipo.PAGAMENTO_REALIZADO, c.getMotoristaId(), "Pagamento recebido no valor de R$ " + String.format("%.2f", p.getValor())));
        return p;
    }

    public double simularPreco(String caronaId) { Carona c = buscarCarona(caronaId); int n=(int)solicitacoes.buscarPorCarona(caronaId).stream().filter(s->s.getStatus().equals("aceita")).count(); return PoliticaPrecoFactory.criar(c.getPoliticaPreco()).calcular(c.getValorBase(), c.getDistanciaKm(), n); }
    public List<Notificacao> notificacoesDoUsuario(String usuarioId) { return new NotificacaoRepository().buscarPorUsuario(usuarioId); }
    private SolicitacaoCarona buscarSolicitacao(String id) { return solicitacoes.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada")); }
    private Carona buscarCarona(String id) { return caronas.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Carona não encontrada")); }
    private String normalizarMetodo(String m) { String x=m==null?"pix":m.toLowerCase(); if(!x.equals("pix")&&!x.equals("dinheiro")) throw new IllegalArgumentException("Método deve ser pix ou dinheiro"); return x; }
}
