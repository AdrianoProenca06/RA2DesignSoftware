package com.example.caronas;

import com.example.caronas.model.Carona;
import com.example.caronas.strategy.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StrategyPatternTest {

    private Carona carona(double km, double base, String politica) {
        return new Carona("c1", "m1", "A", "B", LocalDateTime.now(), 3, km, base, politica);
    }

    @Test
    void precoFixoIgnoraDistanciaEOcupacao() {
        assertEquals(12.0, new PrecoFixo().calcular(carona(50, 12.0, "FIXO"), 0), 0.001);
        assertEquals(12.0, new PrecoFixo().calcular(carona(5, 12.0, "FIXO"), 2), 0.001);
    }

    @Test
    void precoPorKmMultiplicaDistanciaPeloValor() {
        assertEquals(15.0, new PrecoPorKm().calcular(carona(10, 1.5, "POR_KM"), 0), 0.001);
    }

    @Test
    void rateioDivideCustoEntreTodosOsOcupantes() {
        Carona c = carona(20, 1.0, "RATEIO");              // custo total R$ 20
        RateioDeCusto r = new RateioDeCusto();
        assertEquals(10.0, r.calcular(c, 0), 0.001);       // motorista + 1 passageiro = 2
        assertEquals(6.67, r.calcular(c, 1), 0.001);       // 3 ocupantes
        assertEquals(5.0, r.calcular(c, 2), 0.001);        // 4 ocupantes
    }

    @Test
    void contextoTrocaDeEstrategiaEmTempoDeExecucao() {
        CalculadoraPreco calc = new CalculadoraPreco(new PrecoFixo());
        Carona c = carona(10, 2.0, "FIXO");
        assertEquals(2.0, calc.calcular(c, 0), 0.001);
        calc.setEstrategia(new PrecoPorKm());
        assertEquals(20.0, calc.calcular(c, 0), 0.001);
    }

    @Test
    void calcularParaEscolhePelaPoliticaDaCarona() {
        CalculadoraPreco calc = new CalculadoraPreco(new PrecoFixo());
        assertEquals(20.0, calc.calcularPara(carona(10, 2.0, "POR_KM"), 0), 0.001);
        assertEquals(6.67, calc.calcularPara(carona(10, 2.0, "RATEIO"), 1), 0.001); // custo 20 / (motorista + 1 confirmado + novo)
    }

    @Test
    void politicaInexistenteELancadaComoErro() {
        assertThrows(IllegalArgumentException.class, () -> EstrategiasPreco.por("INEXISTENTE"));
    }

    @Test
    void valorInvalidoEBloqueado() {
        CalculadoraPreco calc = new CalculadoraPreco(new PrecoFixo());
        assertThrows(IllegalStateException.class, () -> calc.calcular(carona(10, 0.0, "FIXO"), 0));
    }
}
