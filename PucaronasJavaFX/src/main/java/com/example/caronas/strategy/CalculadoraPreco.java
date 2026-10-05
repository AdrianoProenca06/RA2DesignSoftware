package com.example.caronas.strategy;

import com.example.caronas.model.Carona;

/**
 * Strategy – papel de "Context". Não conhece nenhuma fórmula: apenas delega à estratégia
 * escolhida em tempo de execução (a política de preço gravada em cada carona).
 */
public class CalculadoraPreco {
    private EstrategiaPreco estrategia;

    public CalculadoraPreco(EstrategiaPreco estrategiaInicial) {
        this.estrategia = estrategiaInicial;
    }

    public void setEstrategia(EstrategiaPreco estrategia) {
        this.estrategia = estrategia;
    }

    public double calcular(Carona carona, int passageirosConfirmados) {
        double valor = estrategia.calcular(carona, passageirosConfirmados);
        if (valor <= 0) {
            throw new IllegalStateException("Política '" + estrategia.nome() + "' gerou valor inválido: " + valor);
        }
        return valor;
    }

    /** Atalho: escolhe a estratégia de acordo com a política da carona e calcula. */
    public double calcularPara(Carona carona, int passageirosConfirmados) {
        setEstrategia(EstrategiasPreco.por(carona.getPoliticaPreco()));
        return calcular(carona, passageirosConfirmados);
    }
}
