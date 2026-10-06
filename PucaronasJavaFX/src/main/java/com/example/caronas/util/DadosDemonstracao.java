package com.example.caronas.util;

import com.example.caronas.model.Motorista;
import com.example.caronas.model.Usuario;
import com.example.caronas.model.Veiculo;
import com.example.caronas.service.CaronaService;
import com.example.caronas.service.MotoristaService;
import com.example.caronas.service.UsuarioService;
import com.example.caronas.service.VeiculoService;

import java.io.IOException;
import java.time.LocalDateTime;

/** Cria um conjunto pequeno de dados para demonstrar o fluxo sem precisar cadastrar tudo à mão. */
public final class DadosDemonstracao {
    private static final String EMAIL_MOTORISTA = "carlos.demo@pucpr.br";

    private DadosDemonstracao() {
    }

    /** @return true se criou os dados; false se eles já existiam (operação idempotente). */
    public static boolean popular() throws IOException {
        UsuarioService usuarios = new UsuarioService();
        if (usuarios.buscarPorEmail(EMAIL_MOTORISTA).isPresent()) {
            return false;
        }

        Usuario carlos = usuarios.criar("Carlos (motorista demo)", EMAIL_MOTORISTA, "123", "motorista");
        usuarios.criar("Ana (passageira demo)", "ana.demo@pucpr.br", "123", "passageiro");
        usuarios.criar("Bia (passageira demo)", "bia.demo@pucpr.br", "123", "passageiro");
        usuarios.criar("Davi (passageiro demo)", "davi.demo@pucpr.br", "123", "passageiro");

        Veiculo carro = new VeiculoService().criar("Onix", "ABC1D23", "Prata", 4);
        Motorista motorista = new MotoristaService().criar(carlos.getId(), carro.getId(), "12345678900");

        CaronaService caronas = new CaronaService();
        LocalDateTime amanha = LocalDateTime.now().plusDays(1).withHour(18).withMinute(30).withSecond(0).withNano(0);
        caronas.criar(motorista.getId(), "PUCPR (Curitiba)", "Centro", amanha, 3, 12.0, 8.0, "FIXO");
        caronas.criar(motorista.getId(), "PUCPR (Curitiba)", "São José dos Pinhais", amanha, 3, 20.0, 1.5, "POR_KM");
        caronas.criar(motorista.getId(), "PUCPR (Curitiba)", "Pinhais", amanha.plusHours(1), 3, 15.0, 2.0, "RATEIO");
        return true;
    }
}
