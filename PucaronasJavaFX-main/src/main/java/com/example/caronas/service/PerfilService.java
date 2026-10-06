package com.example.caronas.service;

import com.example.caronas.model.Perfil;
import com.example.caronas.repository.PerfilRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PerfilService {
    private PerfilRepository repository;

    public PerfilService() {
        this.repository = new PerfilRepository();
    }

    public Perfil criar(String usuarioId, String curso, String turno, String telefone) throws IOException {
        if (usuarioId == null || usuarioId.isEmpty()) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }

        Perfil perfil = new Perfil(UUID.randomUUID().toString(), usuarioId, curso, turno, telefone, 0.0);
        repository.inserir(perfil);
        return perfil;
    }

    public List<Perfil> listar() {
        return repository.listar();
    }

    public Optional<Perfil> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public Optional<Perfil> buscarPorUsuarioId(String usuarioId) {
        return repository.buscarPorUsuarioId(usuarioId);
    }

    public List<Perfil> buscarPorCurso(String curso) {
        return repository.buscarPorCurso(curso);
    }

    public Perfil atualizar(String id, String curso, String turno, String telefone, double avaliacaoMedia) throws IOException {
        Optional<Perfil> perfil = repository.buscarPorId(id);
        if (perfil.isEmpty()) {
            throw new IllegalArgumentException("Perfil não encontrado");
        }

        perfil.get().setCurso(curso);
        perfil.get().setTurno(turno);
        perfil.get().setTelefone(telefone);
        perfil.get().setAvaliacaoMedia(avaliacaoMedia);

        repository.atualizar(perfil.get());
        return perfil.get();
    }

    public void excluir(String id) throws IOException {
        Optional<Perfil> perfil = repository.buscarPorId(id);
        if (perfil.isEmpty()) {
            throw new IllegalArgumentException("Perfil não encontrado");
        }
        repository.excluirPorId(id);
    }
}

