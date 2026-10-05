package com.example.caronas.util;

import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;

public class DialogUtil {
    public static void mostrarInfo(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    public static void mostrarErro(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    public static void mostrarAviso(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}

