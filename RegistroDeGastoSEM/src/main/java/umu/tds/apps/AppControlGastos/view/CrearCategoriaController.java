package umu.tds.apps.AppControlGastos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;

public class CrearCategoriaController {

    @FXML
    private TextField txtNombreCategoria;

    @FXML
    public void guardarCategoria(ActionEvent event) {
        String nombre = txtNombreCategoria.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAlerta("Error", "El nombre de la categoría no puede estar vacío.");
            return;
        }
        boolean exito = AppControlGastos.getInstancia().registrarCategoria(nombre);

        if (exito) {
            mostrarAlerta("Éxito", "Categoría '" + nombre + "' creada correctamente.");
            cerrarVentana();
        } else {
            mostrarAlerta("Error", "La categoría ya existe o no se pudo crear.");
        }
    }

    @FXML
    public void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombreCategoria.getScene().getWindow();
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