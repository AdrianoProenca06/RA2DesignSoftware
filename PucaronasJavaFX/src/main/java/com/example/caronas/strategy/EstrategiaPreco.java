package com.example.caronas.strategy;

import com.example.caronas.model.Carona;

/**
 * PADRÃO: Strategy (GoF - comportamental) – papel de "Strategy".
 *
 * <p>Define o contrato de uma família de algoritmos intercambiáveis para calcular quanto
 * UM passageiro paga por uma carona. Cada política de preço é uma classe concreta.
 */
public interface EstrategiaPreco {

    /** Identificador gravado em {@code Carona.politicaPreco}. */
    String nome();

    /** Descrição curta exibida na interface. */
    String descricao();

    /**
     * @param carona                  carona sendo precificada
     * @param passageirosConfirmados  passageiros que já tiveram a solicitação aceita
     *                                (sem contar o passageiro que está sendo precificado)
     * @return valor, em reais, a ser cobrado do novo passageiro
     */
    double calcular(Carona carona, int passageirosConfirmados);
}
