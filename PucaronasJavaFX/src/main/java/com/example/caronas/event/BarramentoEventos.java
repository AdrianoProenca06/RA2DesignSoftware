package com.example.caronas.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PADRÃO: Observer – papel de "Subject" (publicador).
 * PADRÃO: Singleton (GoF - criacional) – existe uma única instância no sistema.
 *
 * <p>Por que Singleton aqui? Os observadores são registrados uma vez (na inicialização da
 * aplicação) e os eventos são publicados por vários pontos (facade, serviços). Se existissem
 * várias instâncias, um evento publicado numa delas não chegaria aos observadores registrados
 * em outra. A instância é criada de forma preguiçosa e thread-safe pelo idiom
 * "initialization-on-demand holder".
 */
public final class BarramentoEventos {

    private static final class Holder {
        private static final BarramentoEventos INSTANCIA = new BarramentoEventos();
    }

    private final List<ObservadorEventos> observadores = new CopyOnWriteArrayList<>();

    private BarramentoEventos() {
    }

    public static BarramentoEventos getInstancia() {
        return Holder.INSTANCIA;
    }

    public void registrar(ObservadorEventos observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void remover(ObservadorEventos observador) {
        observadores.remove(observador);
    }

    /**
     * Notifica todos os observadores. A falha de um observador não pode impedir os demais
     * nem desfazer a operação de negócio que gerou o evento, por isso o erro é apenas registrado.
     */
    public void publicar(EventoCarona evento) {
        for (ObservadorEventos o : observadores) {
            try {
                o.atualizar(evento);
            } catch (Exception e) {
                System.err.println("Observador " + o.getClass().getSimpleName()
                        + " falhou ao tratar " + evento.tipo() + ": " + e.getMessage());
            }
        }
    }

    /** Remove todos os observadores. Útil em testes, já que o Singleton mantém estado global. */
    public void limpar() {
        observadores.clear();
    }

    public int totalObservadores() {
        return observadores.size();
    }
}
