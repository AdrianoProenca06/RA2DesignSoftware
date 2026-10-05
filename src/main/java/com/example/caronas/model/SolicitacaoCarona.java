package com.example.caronas.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SolicitacaoCarona implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String passageiroId;
    private String caronaId;
    private String status; // "pendente", "aceita" ou "recusada"
    private LocalDateTime dataSolicitacao;

    public SolicitacaoCarona() {
    }

    public SolicitacaoCarona(String id, String passageiroId, String caronaId, String status, LocalDateTime dataSolicitacao) {
        this.id = id;
        this.passageiroId = passageiroId;
        this.caronaId = caronaId;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassageiroId() {
        return passageiroId;
    }

    public void setPassageiroId(String passageiroId) {
        this.passageiroId = passageiroId;
    }

    public String getCaronaId() {
        return caronaId;
    }

    public void setCaronaId(String caronaId) {
        this.caronaId = caronaId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    @Override
    public String toString() {
        return "SolicitacaoCarona{" +
                "id='" + id + '\'' +
                ", passageiroId='" + passageiroId + '\'' +
                ", caronaId='" + caronaId + '\'' +
                ", status='" + status + '\'' +
                ", dataSolicitacao=" + dataSolicitacao +
                '}';
    }
}

