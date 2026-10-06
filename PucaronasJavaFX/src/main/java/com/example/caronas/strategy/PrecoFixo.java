package com.example.caronas.strategy;

import com.example.caronas.model.Carona;

/** Strategy concreta: cada passageiro paga o valor base informado, independente da distância. */
public class PrecoFixo implements EstrategiaPreco {
    public static final String NOME = "FIXO";

    @Override
    public String nome() {
        return NOME;
    }

    @Override
    public String descricao() {
        return "Valor fixo por passageiro";
    }

    @Override
    public double calcular(Carona carona, int passageirosConfirmados) {
        return carona.getValorBase();
    }
}
