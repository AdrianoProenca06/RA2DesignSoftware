package com.example.caronas.service;

import com.example.caronas.model.Usuario;
import com.example.caronas.repository.UsuarioRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioService {
    private UsuarioRepository repository;

    public UsuarioService() {
        this.repository = new UsuarioRepository();
    }

    public Usuario criar(String nome, String email, String senha, String tipo) throws IOException {
        if (nome == null || nome.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Nome e email são obrigatórios");
        }

        Optional<Usuario> existente = repository.buscarPorEmail(email);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario usuario = new Usuario(UUID.randomUUID().toString(), nome, email, senha, tipo);
        repository.inserir(usuario);
        return usuario;
    }

    public List<Usuario> listar() {
        return repository.listar();
    }

    public Optional<Usuario> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.buscarPorEmail(email);
    }

    public List<Usuario> buscarPorTipo(String tipo) {
        return repository.buscarPorTipo(tipo);
    }

    public Usuario atualizar(String id, String nome, String email, String senha, String tipo) throws IOException {
        Optional<Usuario> usuario = repository.buscarPorId(id);
        if (usuario.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        usuario.get().setNome(nome);
        usuario.get().setEmail(email);
        usuario.get().setSenha(senha);
        usuario.get().setTipo(tipo);

        repository.atualizar(usuario.get());
        return usuario.get();
    }

    public void excluir(String id) throws IOException {
        Optional<Usuario> usuario = repository.buscarPorId(id);
        if (usuario.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        repository.excluirPorId(id);
    }

    public boolean autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.buscarPorEmail(email);
        return usuario.isPresent() && usuario.get().getSenha().equals(senha);
    }
}

