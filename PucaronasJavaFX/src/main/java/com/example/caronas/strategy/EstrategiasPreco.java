package com.example.caronas.strategy;

import java.util.LinkedHashMap;
import java.util.Map;

/** Catálogo das estratégias disponíveis (consulta por nome). Para criar uma nova política, basta registrá-la aqui. */
public final class EstrategiasPreco {
    private static final Map<String, EstrategiaPreco> CATALOGO = new LinkedHashMap<>();

    static {
        registrar(new PrecoFixo());
        registrar(new PrecoPorKm());
        registrar(new RateioDeCusto());
    }

    private EstrategiasPreco() {
    }

    public static void registrar(EstrategiaPreco estrategia) {
        CATALOGO.put(estrategia.nome(), estrategia);
    }

    public static EstrategiaPreco por(String nome) {
        EstrategiaPreco e = CATALOGO.get(nome == null ? PrecoFixo.NOME : nome);
        if (e == null) {
            throw new IllegalArgumentException("Política de preço desconhecida: " + nome);
        }
        return e;
    }

    public static java.util.List<String> nomes() {
        return java.util.List.copyOf(CATALOGO.keySet());
    }
}
