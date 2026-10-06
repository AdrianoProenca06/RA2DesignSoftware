package com.example.caronas.repository;

import com.example.caronas.model.Veiculo;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VeiculoRepository extends BaseRepository<Veiculo> {
    
    public VeiculoRepository() {
        super("veiculos");
    }

    public Optional<Veiculo> buscarPorId(String id) {
        List<Veiculo> veiculos = listar();
        return veiculos.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    public Optional<Veiculo> buscarPorPlaca(String placa) {
        List<Veiculo> veiculos = listar();
        return veiculos.stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                .findFirst();
    }

    public List<Veiculo> buscarPorModelo(String modelo) {
        List<Veiculo> veiculos = listar();
        return veiculos.stream()
                .filter(v -> v.getModelo().equalsIgnoreCase(modelo))
                .toList();
    }

    @Override
    public void atualizar(Veiculo veiculo) throws IOException {
        List<Veiculo> veiculos = listar();
        for (int i = 0; i < veiculos.size(); i++) {
            if (veiculos.get(i).getId().equals(veiculo.getId())) {
                veiculos.set(i, veiculo);
                break;
            }
        }
        salvarLista(veiculos);
    }

    @Override
    public void excluir(Veiculo veiculo) throws IOException {
        List<Veiculo> veiculos = listar();
        veiculos.removeIf(v -> v.getId().equals(veiculo.getId()));
        salvarLista(veiculos);
    }

    public void excluirPorId(String id) throws IOException {
        List<Veiculo> veiculos = listar();
        veiculos.removeIf(v -> v.getId().equals(id));
        salvarLista(veiculos);
    }
}

