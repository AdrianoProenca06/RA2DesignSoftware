package com.example.caronas.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HistoricoCorrida implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private LocalDateTime data;
    private String motoristaId;
    private String passageiroId;
    private String trajeto;
    private double valor;

    public HistoricoCorrida() {
    }

    public HistoricoCorrida(String id, LocalDateTime data, String motoristaId, String passageiroId, String trajeto, double valor) {
        this.id = id;
        this.data = data;
        this.motoristaId = motoristaId;
        this.passageiroId = passageiroId;
        this.trajeto = trajeto;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getMotoristaId() {
        return motoristaId;
    }

    public void setMotoristaId(String motoristaId) {
        this.motoristaId = motoristaId;
    }

    public String getPassageiroId() {
        return passageiroId;
    }

    public void setPassageiroId(String passageiroId) {
        this.passageiroId = passageiroId;
    }

    public String getTrajeto() {
        return trajeto;
    }

    public void setTrajeto(String trajeto) {
        this.trajeto = trajeto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "HistoricoCorrida{" +
                "id='" + id + '\'' +
                ", data=" + data +
                ", motoristaId='" + motoristaId + '\'' +
                ", passageiroId='" + passageiroId + '\'' +
                ", trajeto='" + trajeto + '\'' +
                ", valor=" + valor +
                '}';
    }
}

