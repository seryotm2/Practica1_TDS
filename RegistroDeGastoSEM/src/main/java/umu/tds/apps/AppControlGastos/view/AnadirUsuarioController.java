package umu.tds.apps.AppControlGastos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;

public class AnadirUsuarioController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;

    @FXML
    public void registrarUsuario(ActionEvent event) {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();

        if (nombre.isBlank() || email.isBlank()) {
            mostrarAlerta("Error", "Debes rellenar nombre y email.");
            return;
        }

        boolean exito = AppControlGastos.getInstancia().registrarUsuario(nombre, email);

        if (exito) {
            mostrarAlerta("Éxito", "Usuario registrado correctamente.");
            txtNombre.clear();
            txtEmail.clear();
        } else {
            mostrarAlerta("Error", "El usuario ya existe.");
        }
    }

    @FXML
    public void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}