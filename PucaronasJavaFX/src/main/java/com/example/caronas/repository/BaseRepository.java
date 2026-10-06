package com.example.caronas.repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * PADRÃO: Template Method (GoF - comportamental).
 *
 * <p>Esta classe define o ESQUELETO das operações de persistência (inserir, atualizar, excluir,
 * buscarPorId) que antes estavam copiadas, de forma idêntica, nos 11 repositórios concretos.
 * Os passos que variam de entidade para entidade são deixados como ganchos (hooks):
 * <ul>
 *   <li>{@link #idDe(Object)} – passo abstrato: como obter o identificador da entidade;</li>
 *   <li>{@link #validar(Object)} – passo opcional (hook com implementação padrão vazia),
 *       sobrescrito por {@link CaronaRepository} para garantir uma invariante do domínio.</li>
 * </ul>
 * Os métodos-template são {@code final} para que as subclasses não quebrem a ordem do algoritmo.
 */
public abstract class BaseRepository<T> {
    protected String fileName;
    protected String dataDir = System.getProperty("caronas.data.dir", "data");

    public BaseRepository(String fileName) {
        this.fileName = fileName;
        criarPastasSeNecessario();
    }

    // ------------------------------------------------------------------
    // Passos que as subclasses fornecem (ganchos do Template Method)
    // ------------------------------------------------------------------

    /** Passo ABSTRATO: devolve o identificador único da entidade. */
    protected abstract String idDe(T entidade);

    /** Gancho opcional: lança IllegalArgumentException se a entidade for inválida. */
    protected void validar(T entidade) {
        // implementação padrão: nada a validar
    }

    // ------------------------------------------------------------------
    // Métodos-template (esqueleto do algoritmo, não sobrescrevíveis)
    // ------------------------------------------------------------------

    public final void inserir(T objeto) throws IOException {
        validar(objeto);                       // passo 1 (gancho)
        List<T> lista = listar();              // passo 2
        lista.add(objeto);                     // passo 3
        salvarLista(lista);                    // passo 4
    }

    public final void atualizar(T objeto) throws IOException {
        validar(objeto);                       // passo 1 (gancho)
        List<T> lista = listar();
        boolean encontrado = false;
        for (int i = 0; i < lista.size(); i++) {
            if (Objects.equals(idDe(lista.get(i)), idDe(objeto))) {
                lista.set(i, objeto);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            throw new IllegalArgumentException("Registro não encontrado: " + idDe(objeto));
        }
        salvarLista(lista);
    }

    public final void excluir(T objeto) throws IOException {
        excluirPorId(idDe(objeto));
    }

    public final void excluirPorId(String id) throws IOException {
        List<T> lista = listar();
        lista.removeIf(e -> Objects.equals(idDe(e), id));
        salvarLista(lista);
    }

    public final Optional<T> buscarPorId(String id) {
        return listar().stream()
                .filter(e -> Objects.equals(idDe(e), id))
                .findFirst();
    }

    // ------------------------------------------------------------------
    // Infraestrutura comum
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public List<T> listar() {
        try {
            File file = new File(getFilePath());
            if (!file.exists()) {
                return new ArrayList<>();
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return new ArrayList<>((List<T>) ois.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao listar dados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    protected void salvarLista(List<T> lista) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getFilePath()))) {
            oos.writeObject(lista);
            oos.flush();
        }
    }

    public void limpar() throws IOException {
        salvarLista(new ArrayList<>());
    }

    protected String getFilePath() {
        return dataDir + File.separator + fileName + ".dat";
    }

    private void criarPastasSeNecessario() {
        try {
            Path path = Paths.get(dataDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de dados: " + e.getMessage());
        }
    }
}
