package com.example.caronas.strategy;
public class PrecoFixoStrategy implements PoliticaPreco {
    public double calcular(double valorBase, double distanciaKm, int passageirosConfirmados) { return valorBase; }
    public String getNome() { return "FIXO"; }
}
