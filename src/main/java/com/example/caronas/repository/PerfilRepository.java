package com.example.caronas.repository;

import com.example.caronas.model.Perfil;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PerfilRepository extends BaseRepository<Perfil> {
    
    public PerfilRepository() {
        super("perfis");
    }

    public Optional<Perfil> buscarPorId(String id) {
        List<Perfil> perfis = listar();
        return perfis.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Optional<Perfil> buscarPorUsuarioId(String usuarioId) {
        List<Perfil> perfis = listar();
        return perfis.stream()
                .filter(p -> p.getUsuarioId().equals(usuarioId))
                .findFirst();
    }

    public List<Perfil> buscarPorCurso(String curso) {
        List<Perfil> perfis = listar();
        return perfis.stream()
                .filter(p -> p.getCurso().equals(curso))
                .toList();
    }

    @Override
    public void atualizar(Perfil perfil) throws IOException {
        List<Perfil> perfis = listar();
        for (int i = 0; i < perfis.size(); i++) {
            if (perfis.get(i).getId().equals(perfil.getId())) {
                perfis.set(i, perfil);
                break;
            }
        }
        salvarLista(perfis);
    }

    @Override
    public void excluir(Perfil perfil) throws IOException {
        List<Perfil> perfis = listar();
        perfis.removeIf(p -> p.getId().equals(perfil.getId()));
        salvarLista(perfis);
    }

    public void excluirPorId(String id) throws IOException {
        List<Perfil> perfis = listar();
        perfis.removeIf(p -> p.getId().equals(id));
        salvarLista(perfis);
    }
}

