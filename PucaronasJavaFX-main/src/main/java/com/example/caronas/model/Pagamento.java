package com.example.caronas.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Pagamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String solicitacaoCaronaId;
    private double valor;
    private String status; // "pendente", "pago" ou "cancelado"
    private String metodo; // "pix" ou "dinheiro"
    private LocalDateTime dataPagamento;

    public Pagamento() {
    }

    public Pagamento(String id, String solicitacaoCaronaId, double valor, String status, String metodo, LocalDateTime dataPagamento) {
        this.id = id;
        this.solicitacaoCaronaId = solicitacaoCaronaId;
        this.valor = valor;
        this.status = status;
        this.metodo = metodo;
        this.dataPagamento = dataPagamento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSolicitacaoCaronaId() {
        return solicitacaoCaronaId;
    }

    public void setSolicitacaoCaronaId(String solicitacaoCaronaId) {
        this.solicitacaoCaronaId = solicitacaoCaronaId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id='" + id + '\'' +
                ", solicitacaoCaronaId='" + solicitacaoCaronaId + '\'' +
                ", valor=" + valor +
                ", status='" + status + '\'' +
                ", metodo='" + metodo + '\'' +
                ", dataPagamento=" + dataPagamento +
                '}';
    }
}

