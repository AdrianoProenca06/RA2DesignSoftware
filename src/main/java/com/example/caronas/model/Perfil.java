package com.example.caronas.model;

import java.io.Serializable;

public class Perfil implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String usuarioId;
    private String curso;
    private String turno;
    private String telefone;
    private double avaliacaoMedia;

    public Perfil() {
    }

    public Perfil(String id, String usuarioId, String curso, String turno, String telefone, double avaliacaoMedia) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.curso = curso;
        this.turno = turno;
        this.telefone = telefone;
        this.avaliacaoMedia = avaliacaoMedia;
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "id='" + id + '\'' +
                ", curso='" + curso + '\'' +
                ", turno='" + turno + '\'' +
                ", telefone='" + telefone + '\'' +
                ", avaliacaoMedia=" + avaliacaoMedia +
                '}';
    }
}

