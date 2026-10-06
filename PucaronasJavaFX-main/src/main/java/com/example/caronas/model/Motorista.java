package com.example.caronas.model;

import java.io.Serializable;

public class Motorista implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String usuarioId;
    private String veiculoId;
    private String cnh;
    private String status; // "ativo" ou "inativo"
    private double avaliacao;

    public Motorista() {
    }

    public Motorista(String id, String usuarioId, String veiculoId, String cnh, String status, double avaliacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.veiculoId = veiculoId;
        this.cnh = cnh;
        this.status = status;
        this.avaliacao = avaliacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(String veiculoId) {
        this.veiculoId = veiculoId;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public String toString() {
        return "Motorista{" +
                "id='" + id + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", veiculoId='" + veiculoId + '\'' +
                ", cnh='" + cnh + '\'' +
                ", status='" + status + '\'' +
                ", avaliacao=" + avaliacao +
                '}';
    }
}

