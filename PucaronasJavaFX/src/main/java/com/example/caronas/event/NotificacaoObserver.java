package com.example.caronas.event;

import com.example.caronas.model.Carona;
import com.example.caronas.model.Motorista;
import com.example.caronas.service.MotoristaService;
import com.example.caronas.service.NotificacaoService;

import java.io.IOException;
import java.util.Locale;

/** Observer concreto: transforma eventos do domínio em Notificações para os usuários afetados. */
public class NotificacaoObserver implements ObservadorEventos {
    private final NotificacaoService notificacoes;
    private final MotoristaService motoristas;

    public NotificacaoObserver(NotificacaoService notificacoes, MotoristaService motoristas) {
        this.notificacoes = notificacoes;
        this.motoristas = motoristas;
    }

    @Override
    public void atualizar(EventoCarona e) {
        try {
            Carona carona = e.carona();
            String rota = carona == null ? "" : carona.getOrigem() + " → " + carona.getDestino();
            switch (e.tipo()) {
                case SOLICITACAO_CRIADA -> notificacoes.criar(usuarioDoMotorista(carona),
                        "Nova solicitação de carona para a rota " + rota + ".");
                case SOLICITACAO_ACEITA -> notificacoes.criar(e.solicitacao().getPassageiroId(),
                        String.format(Locale.US, "Sua solicitação para %s foi ACEITA. Valor a pagar: R$ %.2f.",
                                rota, e.pagamento().getValor()));
                case SOLICITACAO_RECUSADA -> notificacoes.criar(e.solicitacao().getPassageiroId(),
                        "Sua solicitação para " + rota + " foi RECUSADA.");
                case SOLICITACAO_CANCELADA -> notificacoes.criar(usuarioDoMotorista(carona),
                        "Uma solicitação para a rota " + rota + " foi CANCELADA.");
                case PAGAMENTO_REALIZADO -> notificacoes.criar(usuarioDoMotorista(carona),
                        String.format(Locale.US, "Pagamento de R$ %.2f recebido (%s) pela carona %s.",
                                e.pagamento().getValor(), e.pagamento().getMetodo(), rota));
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao gravar notificação", ex);
        }
    }

    /** Carona guarda o id do Motorista; a notificação é enviada ao Usuário dono desse motorista. */
    private String usuarioDoMotorista(Carona carona) {
        return motoristas.buscarPorId(carona.getMotoristaId())
                .map(Motorista::getUsuarioId)
                .orElse(carona.getMotoristaId());
    }
}
