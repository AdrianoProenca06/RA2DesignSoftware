package com.example.caronas.repository;

import com.example.caronas.model.Usuario;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository extends BaseRepository<Usuario> {
    
    public UsuarioRepository() {
        super("usuarios");
    }

    public Optional<Usuario> buscarPorId(String id) {
        List<Usuario> usuarios = listar();
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        List<Usuario> usuarios = listar();
        return usuarios.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    public List<Usuario> buscarPorTipo(String tipo) {
        List<Usuario> usuarios = listar();
        return usuarios.stream()
                .filter(u -> u.getTipo().equals(tipo))
                .toList();
    }

    @Override
    public void atualizar(Usuario usuario) throws IOException {
        List<Usuario> usuarios = listar();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(usuario.getId())) {
                usuarios.set(i, usuario);
                break;
            }
        }
        salvarLista(usuarios);
    }

    @Override
    public void excluir(Usuario usuario) throws IOException {
        List<Usuario> usuarios = listar();
        usuarios.removeIf(u -> u.getId().equals(usuario.getId()));
        salvarLista(usuarios);
    }

    public void excluirPorId(String id) throws IOException {
        List<Usuario> usuarios = listar();
        usuarios.removeIf(u -> u.getId().equals(id));
        salvarLista(usuarios);
    }
}

