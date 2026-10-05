package com.example.caronas;

import com.example.caronas.model.Usuario;
import com.example.caronas.repository.UsuarioRepository;
import org.junit.jupiter.api.*;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTemplateMethodTest {
    private Path dir;
    private UsuarioRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        dir = TestSupport.prepararDadosTemporarios();
        repo = new UsuarioRepository();
    }

    @AfterEach
    void tearDown() throws Exception {
        TestSupport.apagar(dir);
    }

    @Test
    void inserirEBuscarPorId() throws Exception {
        repo.inserir(new Usuario("u1", "Ana", "ana@x.com", "123", "passageiro"));
        assertEquals("Ana", repo.buscarPorId("u1").orElseThrow().getNome());
        assertTrue(repo.buscarPorId("nao-existe").isEmpty());
    }

    @Test
    void atualizarSubstituiOObjetoPersistido() throws Exception {
        repo.inserir(new Usuario("u1", "Ana", "ana@x.com", "123", "passageiro"));
        repo.atualizar(new Usuario("u1", "Ana Maria", "ana@x.com", "123", "motorista"));
        Usuario u = repo.buscarPorId("u1").orElseThrow();
        assertEquals("Ana Maria", u.getNome());
        assertEquals("motorista", u.getTipo());
        assertEquals(1, repo.listar().size());
    }

    @Test
    void atualizarDeRegistroInexistenteFalha() {
        assertThrows(IllegalArgumentException.class,
                () -> repo.atualizar(new Usuario("zzz", "X", "x@x.com", "1", "passageiro")));
    }

    @Test
    void ganchoValidarDaCaronaBarraVagasNegativasEmInserirEAtualizar() throws Exception {
        var caronas = new com.example.caronas.repository.CaronaRepository();
        var c = new com.example.caronas.model.Carona("c1", "m1", "A", "B", java.time.LocalDateTime.now(), 2);
        caronas.inserir(c);

        c.setVagasDisponiveis(-1);
        assertThrows(IllegalArgumentException.class, () -> caronas.atualizar(c));
        assertThrows(IllegalArgumentException.class, () -> caronas.inserir(
                new com.example.caronas.model.Carona("c2", "m1", "A", "B", java.time.LocalDateTime.now(), -3)));
        assertEquals(2, caronas.buscarPorId("c1").orElseThrow().getVagasDisponiveis());
    }

    @Test
    void excluirRemoveSomenteOAlvo() throws Exception {
        repo.inserir(new Usuario("u1", "Ana", "a@x.com", "1", "passageiro"));
        repo.inserir(new Usuario("u2", "Bia", "b@x.com", "1", "passageiro"));
        repo.excluirPorId("u1");
        assertEquals(1, repo.listar().size());
        assertEquals("u2", repo.listar().get(0).getId());
    }
}
