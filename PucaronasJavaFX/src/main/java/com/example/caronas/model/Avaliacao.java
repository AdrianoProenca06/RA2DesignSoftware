package com.example.caronas.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Avaliacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String avaliadorId;
    private String usuarioAvaliadoId;
    private int nota;
    private String comentario;
    private LocalDateTime data;

    public Avaliacao() {
    }

    public Avaliacao(String id, String avaliadorId, String usuarioAvaliadoId, int nota, String comentario, LocalDateTime data) {
        this.id = id;
        this.avaliadorId = avaliadorId;
        this.usuarioAvaliadoId = usuarioAvaliadoId;
        this.nota = nota;
        this.comentario = comentario;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvaliadorId() {
        return avaliadorId;
    }

    public void setAvaliadorId(String avaliadorId) {
        this.avaliadorId = avaliadorId;
    }

    public String getUsuarioAvaliadoId() {
        return usuarioAvaliadoId;
    }

    public void setUsuarioAvaliadoId(String usuarioAvaliadoId) {
        this.usuarioAvaliadoId = usuarioAvaliadoId;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "id='" + id + '\'' +
                ", avaliadorId='" + avaliadorId + '\'' +
                ", usuarioAvaliadoId='" + usuarioAvaliadoId + '\'' +
                ", nota=" + nota +
                ", comentario='" + comentario + '\'' +
                ", data=" + data +
                '}';
    }
}

