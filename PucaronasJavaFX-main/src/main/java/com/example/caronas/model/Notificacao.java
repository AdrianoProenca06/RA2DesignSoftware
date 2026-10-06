package com.example.caronas.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notificacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String usuarioId;
    private String mensagem;
    private LocalDateTime data;
    private boolean lida;

    public Notificacao() {
    }

    public Notificacao(String id, String usuarioId, String mensagem, LocalDateTime data, boolean lida) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.mensagem = mensagem;
        this.data = data;
        this.lida = lida;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "id='" + id + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", data=" + data +
                ", lida=" + lida +
                '}';
    }
}

