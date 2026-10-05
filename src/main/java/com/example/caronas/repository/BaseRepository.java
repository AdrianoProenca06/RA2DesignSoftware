package com.example.caronas.repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class BaseRepository<T> {
    protected String fileName;
    protected String dataDir = "data";

    public BaseRepository(String fileName) {
        this.fileName = fileName;
        criarpastasesedecessario();
    }

    private void criarpastasesedecessario() {
        try {
            Path path = Paths.get(dataDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de dados: " + e.getMessage());
        }
    }

    protected String getFilePath() {
        return dataDir + File.separator + fileName + ".dat";
    }

    public void inserir(T objeto) throws IOException {
        List<T> lista = listar();
        lista.add(objeto);
        salvarLista(lista);
    }

    public List<T> listar() {
        try {
            File file = new File(getFilePath());
            if (!file.exists()) {
                return new ArrayList<>();
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<T>) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao listar dados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void atualizar(T objeto) throws IOException {
        List<T> lista = listar();
        salvarLista(lista);
    }

    public void excluir(T objeto) throws IOException {
        List<T> lista = listar();
        lista.remove(objeto);
        salvarLista(lista);
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
}

