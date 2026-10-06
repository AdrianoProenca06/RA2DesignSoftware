package com.example.caronas.event;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
public final class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    private final List<EventoObserver> observers = new CopyOnWriteArrayList<>();
    private EventBus() {}
    public static EventBus getInstance() { return INSTANCE; }
    public void registrar(EventoObserver o) { if (o != null && !observers.contains(o)) observers.add(o); }
    public void remover(EventoObserver o) { observers.remove(o); }
    public void publicar(EventoCarona e) { observers.forEach(o -> o.atualizar(e)); }
    public void limparObservadores() { observers.clear(); }
}
