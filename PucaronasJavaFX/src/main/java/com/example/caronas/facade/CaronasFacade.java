package com.example.caronas.facade;

import com.example.caronas.event.BarramentoEventos;
import com.example.caronas.event.EventoCarona;
import com.example.caronas.event.TipoEvento;
import com.example.caronas.model.Carona;
import com.example.caronas.model.Pagamento;
import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.service.CaronaService;
import com.example.caronas.service.MotoristaService;
import com.example.caronas.service.PagamentoService;
import com.example.caronas.service.SolicitacaoCaronaService;
import com.example.caronas.state.EstadoAceita;
import com.example.caronas.state.EstadoPendente;
import com.example.caronas.strategy.CalculadoraPreco;
import com.example.caronas.strategy.EstrategiasPreco;

import java.io.IOException;
import java.util.List;

/**
 * PADRÃO: Facade (GoF - estrutural).
 *
 * <p>Os casos de uso do sistema ("solicitar carona", "aceitar solicitação", "pagar", ...) exigem
 * coordenar vários subsistemas: serviços de Carona, Solicitação e Pagamento, a calculadora de
 * preço (Strategy), as transições de estado (State) e a publicação de eventos (Observer).
 * Sem o Facade, cada tela precisaria conhecer todos eles e repetir a coreografia na ordem certa.
 * Com ele, a interface gráfica chama UM método e os detalhes ficam escondidos aqui.
 */
public class CaronasFacade {
    private final CaronaService caronas;
    private final SolicitacaoCaronaService solicitacoes;
    private final PagamentoService pagamentos;
    private final MotoristaService motoristas;
    private final CalculadoraPreco calculadora;
    private final BarramentoEventos eventos;

    public CaronasFacade() {
        this(new CaronaService(), new SolicitacaoCaronaService(), new PagamentoService(),
                new MotoristaService(), BarramentoEventos.getInstancia());
    }

    public CaronasFacade(CaronaService caronas, SolicitacaoCaronaService solicitacoes,
                         PagamentoService pagamentos, MotoristaService motoristas, BarramentoEventos eventos) {
        this.caronas = caronas;
        this.solicitacoes = solicitacoes;
        this.pagamentos = pagamentos;
        this.motoristas = motoristas;
        this.eventos = eventos;
        this.calculadora = new CalculadoraPreco(EstrategiasPreco.por("FIXO"));
    }

    // ------------------------------------------------------------------
    // Casos de uso
    // ------------------------------------------------------------------

    /** Passageiro pede uma vaga. Regras: carona existe, tem vaga, motorista não pede a própria carona, sem duplicidade. */
    public SolicitacaoCarona solicitarCarona(String passageiroId, String caronaId) throws IOException {
        Carona carona = caronas.buscarPorId(caronaId)
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada"));
        if (carona.getVagasDisponiveis() <= 0) {
            throw new IllegalStateException("Esta carona não tem vagas disponíveis");
        }
        boolean ehOMotorista = motoristas.buscarPorId(carona.getMotoristaId())
                .map(m -> m.getUsuarioId().equals(passageiroId)).orElse(false);
        if (ehOMotorista) {
            throw new IllegalStateException("O motorista não pode solicitar a própria carona");
        }
        boolean jaSolicitou = solicitacoes.buscarPorCarona(caronaId).stream()
                .anyMatch(s -> s.getPassageiroId().equals(passageiroId)
                        && (s.getStatus().equals(EstadoPendente.NOME) || s.getStatus().equals(EstadoAceita.NOME)));
        if (jaSolicitou) {
            throw new IllegalStateException("Este passageiro já tem uma solicitação ativa para esta carona");
        }

        SolicitacaoCarona s = solicitacoes.criar(passageiroId, caronaId);
        eventos.publicar(EventoCarona.de(TipoEvento.SOLICITACAO_CRIADA, s, carona, null));
        return s;
    }

    /**
     * Motorista aceita: valida vaga, transiciona o estado, ocupa a vaga, calcula o preço pela
     * política da carona (Strategy), gera o pagamento pendente e avisa os interessados (Observer).
     */
    public Pagamento aceitarSolicitacao(String solicitacaoId) throws IOException {
        SolicitacaoCarona s = solicitacoes.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));
        Carona carona = caronas.buscarPorId(s.getCaronaId())
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada"));
        if (carona.getVagasDisponiveis() <= 0) {
            throw new IllegalStateException("Não há vagas disponíveis para aceitar esta solicitação");
        }

        int jaConfirmados = passageirosConfirmados(carona.getId());
        double valor = calculadora.calcularPara(carona, jaConfirmados);      // pode lançar -> nada foi alterado ainda

        solicitacoes.aceitar(solicitacaoId);                                 // State (lança se transição inválida)
        caronas.reduzirVagas(carona.getId());
        Pagamento pagamento = pagamentos.criar(solicitacaoId, valor, "pix"); // método definitivo é escolhido ao pagar

        SolicitacaoCarona atualizada = solicitacoes.buscarPorId(solicitacaoId).orElseThrow();
        eventos.publicar(EventoCarona.de(TipoEvento.SOLICITACAO_ACEITA, atualizada, carona, pagamento));
        return pagamento;
    }

    public SolicitacaoCarona recusarSolicitacao(String solicitacaoId) throws IOException {
        SolicitacaoCarona s = solicitacoes.recusar(solicitacaoId);           // State
        Carona carona = caronas.buscarPorId(s.getCaronaId()).orElse(null);
        eventos.publicar(EventoCarona.de(TipoEvento.SOLICITACAO_RECUSADA, s, carona, null));
        return s;
    }

    /** Cancelamento: se a solicitação já tinha sido aceita, devolve a vaga e cancela o pagamento pendente. */
    public SolicitacaoCarona cancelarSolicitacao(String solicitacaoId) throws IOException {
        SolicitacaoCarona antes = solicitacoes.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));
        boolean estavaAceita = EstadoAceita.NOME.equals(antes.getStatus());

        if (estavaAceita) {
            boolean temPagamentoPago = pagamentos.buscarPorSolicitacao(solicitacaoId).stream()
                    .anyMatch(p -> "pago".equals(p.getStatus()));
            if (temPagamentoPago) {
                throw new IllegalStateException("Solicitação com pagamento realizado não pode ser cancelada");
            }
        }

        SolicitacaoCarona s = solicitacoes.cancelar(solicitacaoId);          // State
        Carona carona = caronas.buscarPorId(s.getCaronaId()).orElse(null);
        if (estavaAceita) {
            if (carona != null) {
                caronas.aumentarVagas(carona.getId());
            }
            for (Pagamento p : pagamentos.buscarPorSolicitacao(solicitacaoId)) {
                if ("pendente".equals(p.getStatus())) {
                    pagamentos.cancelarPagamento(p.getId());
                }
            }
        }
        eventos.publicar(EventoCarona.de(TipoEvento.SOLICITACAO_CANCELADA, s, carona, null));
        return s;
    }

    /** Passageiro paga o valor combinado. O Observer de histórico registra a corrida. */
    public Pagamento pagar(String pagamentoId, String metodo) throws IOException {
        Pagamento p = pagamentos.buscarPorId(pagamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado"));
        SolicitacaoCarona s = solicitacoes.buscarPorId(p.getSolicitacaoCaronaId())
                .orElseThrow(() -> new IllegalStateException("Solicitação do pagamento não encontrada"));
        if (!EstadoAceita.NOME.equals(s.getStatus())) {
            throw new IllegalStateException("Só é possível pagar solicitações aceitas (status atual: " + s.getStatus() + ")");
        }
        Pagamento pago = pagamentos.realizarPagamento(pagamentoId, metodo);
        Carona carona = caronas.buscarPorId(s.getCaronaId()).orElseThrow();
        eventos.publicar(EventoCarona.de(TipoEvento.PAGAMENTO_REALIZADO, s, carona, pago));
        return pago;
    }

    // ------------------------------------------------------------------
    // Consultas auxiliares
    // ------------------------------------------------------------------

    /** Quanto o PRÓXIMO passageiro pagaria nesta carona, segundo a política dela. */
    public double simularPreco(String caronaId) {
        Carona carona = caronas.buscarPorId(caronaId)
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada"));
        return calculadora.calcularPara(carona, passageirosConfirmados(caronaId));
    }

    public List<Carona> caronasDisponiveis() {
        return caronas.buscarComVagasDisponiveis();
    }

    public List<Pagamento> pagamentosDaSolicitacao(String solicitacaoId) {
        return pagamentos.buscarPorSolicitacao(solicitacaoId);
    }

    private int passageirosConfirmados(String caronaId) {
        return (int) solicitacoes.buscarPorCarona(caronaId).stream()
                .filter(s -> EstadoAceita.NOME.equals(s.getStatus()))
                .count();
    }
}
