package com.example.caronas.strategy;
public final class PoliticaPrecoFactory {
    private PoliticaPrecoFactory() {}
    public static PoliticaPreco criar(String nome) {
        return switch (nome == null ? "FIXO" : nome.toUpperCase()) {
            case "POR_KM" -> new PrecoPorKmStrategy();
            case "RATEIO" -> new PrecoRateioStrategy();
            default -> new PrecoFixoStrategy();
        };
    }
}
