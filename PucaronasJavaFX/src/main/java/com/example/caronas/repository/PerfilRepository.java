package com.example.caronas.repository;

import com.example.caronas.model.Perfil;
import java.util.List;
import java.util.Optional;

public class PerfilRepository extends BaseRepository<Perfil> {
    
    public PerfilRepository() {
        super("perfis");
    }

    @Override
    protected String idDe(Perfil entidade) {
        return entidade.getId();
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

}
