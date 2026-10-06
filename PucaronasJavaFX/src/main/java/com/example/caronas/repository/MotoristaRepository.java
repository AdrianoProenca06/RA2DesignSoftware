package com.example.caronas.repository;

import com.example.caronas.model.Motorista;
import java.util.List;
import java.util.Optional;

public class MotoristaRepository extends BaseRepository<Motorista> {

    public MotoristaRepository() {
        super("motoristas");
    }

    @Override
    protected String idDe(Motorista entidade) {
        return entidade.getId();
    }

    public Optional<Motorista> buscarPorUsuarioId(String usuarioId) {
        List<Motorista> motoristas = listar();
        return motoristas.stream()
                .filter(m -> m.getUsuarioId().equals(usuarioId))
                .findFirst();
    }

    public List<Motorista> buscarPorStatus(String status) {
        List<Motorista> motoristas = listar();
        return motoristas.stream()
                .filter(m -> m.getStatus().equals(status))
                .toList();
    }

}
