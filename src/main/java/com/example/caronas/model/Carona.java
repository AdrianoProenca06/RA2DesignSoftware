package com.example.caronas.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Carona implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String motoristaId;
    private String origem;
    private String destino;
    private LocalDateTime horario;
    private int vagasDisponiveis;

    public Carona() {
    }

    public Carona(String id, String motoristaId, String origem, String destino, LocalDateTime horario, int vagasDisponiveis) {
        this.id = id;
        this.motoristaId = motoristaId;
        this.origem = origem;
        this.destino = destino;
        this.horario = horario;
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMotoristaId() {
        return motoristaId;
    }

    public void setMotoristaId(String motoristaId) {
        this.motoristaId = motoristaId;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public int getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(int vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    @Override
    public String toString() {
        return "Carona{" +
                "id='" + id + '\'' +
                ", motoristaId='" + motoristaId + '\'' +
                ", origem='" + origem + '\'' +
                ", destino='" + destino + '\'' +
                ", horario=" + horario +
                ", vagasDisponiveis=" + vagasDisponiveis +
                '}';
    }
}

