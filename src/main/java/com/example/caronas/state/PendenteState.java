package com.example.caronas.state;
import com.example.caronas.model.SolicitacaoCarona;
public class PendenteState implements SolicitacaoState {
    public void aceitar(SolicitacaoCarona s) { s.mudarEstado(new AceitaState()); }
    public void recusar(SolicitacaoCarona s) { s.mudarEstado(new RecusadaState()); }
    public void cancelar(SolicitacaoCarona s) { s.mudarEstado(new CanceladaState()); }
    public String nome() { return "pendente"; }
}
