package umu.tds.apps.AppControlGastos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Gasto;

import java.util.Optional;

public class EliminarDatosController {

    @FXML private ListView<Gasto> listaGastos;

    @FXML
    public void initialize() {
        cargarGastos();
        
        listaGastos.setCellFactory(param -> new ListCell<Gasto>() {
            @Override
            protected void updateItem(Gasto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%s - %s (%.2f €)", 
                        item.getFecha().toString(), 
                        item.getConcepto(), 
                        item.getCantidad()));
                }
            }
        });
    }

    private void cargarGastos() {
        listaGastos.getItems().setAll(AppControlGastos.getInstancia().obtenerTodosLosGastos());
    }

    @FXML
    public void borrarGasto(ActionEvent event) {
        Gasto seleccionado = listaGastos.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Por favor, selecciona un gasto de la lista.");
            return;
        }

        if (confirmarBorrado()) {
            boolean exito = AppControlGastos.getInstancia().eliminarGasto(seleccionado);
            if (exito) {
                cargarGastos(); 
            } else {
                mostrarAlerta("Error", "No se pudo eliminar el gasto.");
            }
        }
    }

    private boolean confirmarBorrado() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Gasto");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres borrar este gasto permanentemente?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void cerrar(ActionEvent event) {
        Stage stage = (Stage) listaGastos.getScene().getWindow();
        stage.close();
    }
}