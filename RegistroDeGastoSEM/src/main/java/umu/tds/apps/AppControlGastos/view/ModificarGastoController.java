package umu.tds.apps.AppControlGastos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Gasto;

public class ModificarGastoController {

    @FXML private TextField txtConcepto;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker dateFecha;
    @FXML private ComboBox<String> comboCategoria;

    private Gasto gastoAEditar;

    @FXML
    public void initialize() {
        comboCategoria.getItems().setAll(AppControlGastos.getInstancia().getNombresCategorias());
    }

    public void initData(Gasto gasto) {
        this.gastoAEditar = gasto;
        
        this.txtConcepto.setText(gasto.getConcepto());
        this.txtCantidad.setText(String.valueOf(gasto.getCantidad()));
        this.dateFecha.setValue(gasto.getFecha());
        
        if (gasto.getCategoria() != null) {
            this.comboCategoria.setValue(gasto.getCategoria().getNombreCategoria());
        }
    }

    @FXML
    public void guardarCambios(ActionEvent event) {
        try {
            String concepto = txtConcepto.getText();
            String categoria = comboCategoria.getValue();
            double cantidad = Double.parseDouble(txtCantidad.getText());
            var fecha = dateFecha.getValue();

            if (concepto.isEmpty() || fecha == null || categoria == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }

            boolean exito = AppControlGastos.getInstancia().modificarGasto(
                gastoAEditar, concepto, cantidad, fecha, categoria
            );

            if (exito) {
                mostrarAlerta("Éxito", "Gasto modificado correctamente.");
                cancelar(null); 
            } else {
                mostrarAlerta("Error", "No se pudo modificar.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La cantidad debe ser un número.");
        }
    }

    @FXML
    public void cancelar(ActionEvent event) {
        ((Stage) txtConcepto.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}