package com.example.caronas.model;

import java.io.Serializable;

public class Veiculo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String modelo;
    private String placa;
    private String cor;
    private int capacidade;

    public Veiculo() {
    }

    public Veiculo(String id, String modelo, String placa, String cor, int capacidade) {
        this.id = id;
        this.modelo = modelo;
        this.placa = placa;
        this.cor = cor;
        this.capacidade = capacidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "id='" + id + '\'' +
                ", modelo='" + modelo + '\'' +
                ", placa='" + placa + '\'' +
                ", cor='" + cor + '\'' +
                ", capacidade=" + capacidade +
                '}';
    }
}

