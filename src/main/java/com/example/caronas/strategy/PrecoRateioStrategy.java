package com.example.caronas.strategy;
public class PrecoRateioStrategy implements PoliticaPreco {
    public double calcular(double custoTotal, double distanciaKm, int passageirosConfirmados) { return custoTotal / (passageirosConfirmados + 2.0); }
    public String getNome() { return "RATEIO"; }
}
