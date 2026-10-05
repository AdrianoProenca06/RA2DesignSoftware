package com.example.caronas.strategy;

import com.example.caronas.model.Carona;

/** Strategy concreta: valor base é o preço por km; passageiro paga distância × preço/km. */
public class PrecoPorKm implements EstrategiaPreco {
    public static final String NOME = "POR_KM";

    @Override
    public String nome() {
        return NOME;
    }

    @Override
    public String descricao() {
        return "Valor por km (distância × valor/km)";
    }

    @Override
    public double calcular(Carona carona, int passageirosConfirmados) {
        return arredondar(carona.getDistanciaKm() * carona.getValorBase());
    }

    static double arredondar(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
