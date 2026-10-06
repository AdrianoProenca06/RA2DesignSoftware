package com.example.caronas.strategy;

import com.example.caronas.model.Carona;

/**
 * Strategy concreta: valor base é o custo do trajeto por km. O custo total (distância × custo/km)
 * é dividido entre TODOS os ocupantes do carro: motorista + passageiros já confirmados + o novo
 * passageiro. Quanto mais gente no carro, menos cada um paga (lógica típica de carona solidária).
 */
public class RateioDeCusto implements EstrategiaPreco {
    public static final String NOME = "RATEIO";

    @Override
    public String nome() {
        return NOME;
    }

    @Override
    public String descricao() {
        return "Rateio do custo entre motorista e passageiros";
    }

    @Override
    public double calcular(Carona carona, int passageirosConfirmados) {
        double custoTotal = carona.getDistanciaKm() * carona.getValorBase();
        int ocupantes = 1 /* motorista */ + passageirosConfirmados + 1 /* novo passageiro */;
        return PrecoPorKm.arredondar(custoTotal / ocupantes);
    }
}
