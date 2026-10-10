package com.example.caronas.strategy;
public class PrecoPorKmStrategy implements PoliticaPreco {
    public double calcular(double valorPorKm, double distanciaKm, int passageirosConfirmados) { return valorPorKm * distanciaKm; }
    public String getNome() { return "POR_KM"; }
}
