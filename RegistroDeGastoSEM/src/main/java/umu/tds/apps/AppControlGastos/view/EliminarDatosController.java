package umu.tds.apps.AppControlGastos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Gasto;

import java.io.IOException;
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
                    setText(String.format("%s - %s (%.2f €) [%s]", 
                        item.getFecha(), 
                        item.getConcepto(), 
                        item.getCantidad(),
                        item.getCategoria() != null ? item.getCategoria().getNombreCategoria() : "Sin Cat."));
                }
            }
        });
    }

    private void cargarGastos() {
        listaGastos.getItems().setAll(AppControlGastos.getInstancia().obtenerTodosLosGastos());
    }

    // --- NUEVO MÉTODO PARA MODIFICAR ---
    @FXML
    public void modificarGasto(ActionEvent event) {
        Gasto seleccionado = listaGastos.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un gasto para modificar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/tds/view/modificar_gasto.fxml"));
            Parent root = loader.load();

            // Pasamos el gasto a la nueva ventana
            ModificarGastoController controller = loader.getController();
            controller.initData(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Modificar Gasto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) listaGastos.getScene().getWindow()));
            stage.showAndWait();

            // Recargamos la lista al volver
            cargarGastos();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de edición.");
        }
    }
    // -----------------------------------

    @FXML
    public void borrarGasto(ActionEvent event) {
        Gasto seleccionado = listaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un gasto.");
            return;
        }
        if (confirmarBorrado()) {
            if (AppControlGastos.getInstancia().eliminarGasto(seleccionado)) {
                cargarGastos(); 
            } else {
                mostrarAlerta("Error", "No se pudo eliminar.");
            }
        }
    }

    private boolean confirmarBorrado() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText(null);
        alert.setContentText("¿Borrar gasto permanentemente?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void cerrar(ActionEvent event) {
        ((Stage) listaGastos.getScene().getWindow()).close();
    }
}	