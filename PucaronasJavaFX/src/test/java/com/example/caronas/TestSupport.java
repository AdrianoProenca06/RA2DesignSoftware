package com.example.caronas;

import com.example.caronas.event.BarramentoEventos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Isola os testes da pasta "data" real: aponta o diretório de persistência para uma pasta
 * temporária e limpa o estado global do Singleton antes de cada teste.
 */
public final class TestSupport {
    private TestSupport() {
    }

    public static Path prepararDadosTemporarios() throws IOException {
        Path dir = Files.createTempDirectory("caronas-test-");
        System.setProperty("caronas.data.dir", dir.toString());
        BarramentoEventos.getInstancia().limpar();
        return dir;
    }

    public static void apagar(Path dir) throws IOException {
        try (Stream<Path> s = Files.walk(dir)) {
            s.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        }
        System.clearProperty("caronas.data.dir");
        BarramentoEventos.getInstancia().limpar();
    }
}
