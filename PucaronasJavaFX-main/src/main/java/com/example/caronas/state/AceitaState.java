package com.example.caronas.state;
import com.example.caronas.model.SolicitacaoCarona;
public class AceitaState implements SolicitacaoState {
    public void cancelar(SolicitacaoCarona s) { s.mudarEstado(new CanceladaState()); }
    public String nome() { return "aceita"; }
}
