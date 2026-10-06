package com.example.caronas.repository;

import com.example.caronas.model.Veiculo;
import java.util.List;
import java.util.Optional;

public class VeiculoRepository extends BaseRepository<Veiculo> {
    
    public VeiculoRepository() {
        super("veiculos");
    }

    @Override
    protected String idDe(Veiculo entidade) {
        return entidade.getId();
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

}
