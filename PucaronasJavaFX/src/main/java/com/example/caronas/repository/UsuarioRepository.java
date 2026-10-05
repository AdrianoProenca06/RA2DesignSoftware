package com.example.caronas.repository;

import com.example.caronas.model.Usuario;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository extends BaseRepository<Usuario> {
    
    public UsuarioRepository() {
        super("usuarios");
    }

    @Override
    protected String idDe(Usuario entidade) {
        return entidade.getId();
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

}
