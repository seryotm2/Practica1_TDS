package umu.tds.apps.AppControlGastos.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Directorio;
import umu.tds.modeloNegocio.Usuario;

import java.util.ArrayList;

public class CrearCuentaController {

    @FXML private TextField txtNombreCuenta;
    @FXML private ComboBox<Usuario> comboUsuarios; 
    @FXML private ListView<Usuario> listaParticipantes; 

    private ObservableList<Usuario> usuariosSeleccionados;

    @FXML
    public void initialize() {
        usuariosSeleccionados = FXCollections.observableArrayList();
        listaParticipantes.setItems(usuariosSeleccionados);

        StringConverter<Usuario> converter = new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario u) {
                return u == null ? "" : u.getNombre() + " (" + u.getEmail() + ")";
            }
            @Override
            public Usuario fromString(String string) { return null; }
        };
        
        comboUsuarios.setConverter(converter);
        comboUsuarios.getItems().addAll(Directorio.getInstancia().getUsuarios().values());
        
        Usuario prop = AppControlGastos.getInstancia().getUsuarioActual();
        comboUsuarios.getItems().removeIf(u -> u.equals(prop));
    }

    @FXML
    public void anadirParticipante(ActionEvent event) {
        Usuario seleccionado = comboUsuarios.getValue();

        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un usuario del desplegable.");
            return;
        }

        if (!usuariosSeleccionados.contains(seleccionado)) {
            usuariosSeleccionados.add(seleccionado);
            comboUsuarios.getSelectionModel().clearSelection(); 
        } else {
            mostrarAlerta("Aviso", "Este usuario ya está en la lista.");
        }
    }

    @FXML
    public void crearCuenta(ActionEvent event) {
        String nombreCuenta = txtNombreCuenta.getText();
        if (nombreCuenta.isBlank() || usuariosSeleccionados.isEmpty()) {
            mostrarAlerta("Error", "Pon un nombre y añade al menos un participante.");
            return;
        }

        boolean exito = AppControlGastos.getInstancia().crearCuentaCompartida(nombreCuenta, new ArrayList<>(usuariosSeleccionados));

        if (exito) {
            mostrarAlerta("Éxito", "Grupo creado correctamente.");
            cerrarVentana();
        } else {
            mostrarAlerta("Error", "No se pudo crear la cuenta.");
        }
    }

    @FXML
    public void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombreCuenta.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}