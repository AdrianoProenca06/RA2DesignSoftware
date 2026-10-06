package com.example.caronas.strategy;

public interface PoliticaPreco {
    double calcular(double valorBase, double distanciaKm, int passageirosConfirmados);
    String getNome();
}
