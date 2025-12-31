package umu.tds.apps.AppControlGastos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;
import java.time.LocalDate;

public class AnadirGastoController {

    @FXML private TextField txtConcepto;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker datePickerFecha;

    @FXML
    public void initialize() {
        datePickerFecha.setValue(LocalDate.now());
        comboCategoria.getItems().addAll(AppControlGastos.getInstancia().getNombresCategorias());
    }

    @FXML
    public void guardarGasto(ActionEvent event) {
        String concepto = txtConcepto.getText();
        String categoria = comboCategoria.getValue();
        if (categoria == null || categoria.isBlank()) {
            categoria = comboCategoria.getEditor().getText(); 
        }
        
        LocalDate fecha = datePickerFecha.getValue();
        double cantidad;

        try {
            cantidad = Double.parseDouble(txtCantidad.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La cantidad debe ser un número válido.");
            return;
        }

        if (concepto.isBlank() || categoria.isBlank() || fecha == null) {
            mostrarAlerta("Error", "Rellena todos los campos.");
            return;
        }

        boolean exito = AppControlGastos.getInstancia().registrarGasto(concepto, categoria, cantidad, fecha);

        if (exito) {
            mostrarAlerta("Éxito", "Gasto guardado correctamente.");
            cerrarVentana();
        } else {
            mostrarAlerta("Error", "No se pudo guardar.");
        }
    }

    @FXML
    public void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtConcepto.getScene().getWindow();
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