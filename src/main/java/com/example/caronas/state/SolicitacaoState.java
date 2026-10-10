package com.example.caronas.state;
import com.example.caronas.model.SolicitacaoCarona;
import java.io.Serializable;
public interface SolicitacaoState extends Serializable {
    default void aceitar(SolicitacaoCarona s) { throw new IllegalStateException("Transição inválida: " + nome() + " -> aceita"); }
    default void recusar(SolicitacaoCarona s) { throw new IllegalStateException("Transição inválida: " + nome() + " -> recusada"); }
    default void cancelar(SolicitacaoCarona s) { throw new IllegalStateException("Transição inválida: " + nome() + " -> cancelada"); }
    String nome();
}
