package com.example.caronas.repository;

import com.example.caronas.model.Motorista;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MotoristaRepository extends BaseRepository<Motorista> {

    public MotoristaRepository() {
        super("motoristas");
    }

    public Optional<Motorista> buscarPorId(String id) {
        List<Motorista> motoristas = listar();
        return motoristas.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
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

    @Override
    public void atualizar(Motorista motorista) throws IOException {
        List<Motorista> motoristas = listar();
        for (int i = 0; i < motoristas.size(); i++) {
            if (motoristas.get(i).getId().equals(motorista.getId())) {
                motoristas.set(i, motorista);
                break;
            }
        }
        salvarLista(motoristas);
    }

    @Override
    public void excluir(Motorista motorista) throws IOException {
        List<Motorista> motoristas = listar();
        motoristas.removeIf(m -> m.getId().equals(motorista.getId()));
        salvarLista(motoristas);
    }

    public void excluirPorId(String id) throws IOException {
        List<Motorista> motoristas = listar();
        motoristas.removeIf(m -> m.getId().equals(id));
        salvarLista(motoristas);
    }
}

