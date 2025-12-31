package umu.tds.apps.AppControlGastos.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Gasto;
import umu.tds.modeloNegocio.buscadores.BuscadorGastos;

public class PanelBusquedaController {

	@FXML private DatePicker dpFechaInicio;
	@FXML private DatePicker dpFechaFin;

	@FXML private TextField txtConcepto;

	@FXML private TextField txtCantidadMin;
	@FXML private TextField txtCantidadMax;

	@FXML private ComboBox<String> cbCategoria;

	@FXML private ListView<String> lvResultados;

	
	/* 
	 *  INICIALIZACIÓN
	 */

	@FXML
	private void initialize() {
		// Inicialización básica de la vista
		cbCategoria.getItems().setAll(AppControlGastos.getInstancia().getNombresCategorias());

		lvResultados.setPlaceholder(
				new Label("No hay resultados de búsqueda")
				);
	}

	
	@FXML
	private void onBuscar() {

		BuscadorGastos buscador = crearBuscadorDesdeFormulario();

		if (buscador == null) {
			return; // no se pudo crear el buscador (datos inválidos)
		}

		Set<Gasto> resultados = AppControlGastos.getInstancia().buscarGasto(buscador);

		mostrarResultados(resultados);
	}

	/*
	 *  CREACIÓN DEL BUSCADOR
	 */

	private BuscadorGastos crearBuscadorDesdeFormulario() {

		BuscadorGastos buscador = null;

		// --- Fecha ---
		LocalDate fInicio = dpFechaInicio.getValue();
		LocalDate fFin = dpFechaFin.getValue();

		if (fInicio != null && fFin != null) {
			if(fInicio.isAfter(fFin))
				mostrarAlerta("Error", "La fecha de inicio debe ser anterior o igual a la fecha de fin.");
			else
				buscador = AppControlGastos.getInstancia().crearBuscadorFecha(fInicio, fFin);
		}

		// --- Concepto ---
		String concepto = txtConcepto.getText();
		if (concepto != null && !concepto.isBlank()) {
			buscador = (buscador == null)
					? AppControlGastos.getInstancia().crearBuscadorConcepto(concepto)
							: AppControlGastos.getInstancia().BuscadorAddConcepto(buscador, concepto);
		}

		// --- Cantidad ---
		Double min = parseDouble(txtCantidadMin.getText());
		Double max = parseDouble(txtCantidadMax.getText());

		if (min != null && max != null) {
			buscador = (buscador == null)
					? AppControlGastos.getInstancia().crearBuscadorCantidad(min, max)
							: AppControlGastos.getInstancia().BuscadorAddCantidad(buscador, min, max);
		}

		// --- Categoría ---
		String nombreCategoria = cbCategoria.getValue();
		if (nombreCategoria != null 
				&& AppControlGastos.getInstancia().existeCategoria(nombreCategoria)) {
			buscador = (buscador == null)
					? AppControlGastos.getInstancia().crearBuscadorCategoria(nombreCategoria)
							: AppControlGastos.getInstancia().BuscadorAddCategoria(buscador, nombreCategoria);
		}
		return buscador;
	}

	/* 
	 *  MOSTRAR RESULTADOS
	 */

	private void mostrarResultados(Set<Gasto> resultados) {
		// Formatear salida
		if(resultados.isEmpty()) {
			mostrarAlerta("", "No hay resultados");
			lvResultados.getItems().setAll();
		}
		Set<String> text = resultados.stream()
				.map(g-> String.format("%s | %s | %.2f € | Categoría %s", 
						g.getFecha().toString(), 
						g.getConcepto(), 
						g.getCantidad(),
						g.getCategoria().getNombreCategoria())
						)
				.collect(Collectors.toSet());
		
		lvResultados.getItems().setAll(text);
	}

	/* 
	 *  UTILIDADES
	 */

	private Double parseDouble(String texto) {
		try {
			return (texto == null || texto.isBlank())
					? null
							: Double.parseDouble(texto);
		} catch (NumberFormatException e) {
			mostrarAlerta("Error", "La cantidad debe ser un número válido.");
			return null;
		}
	}
	
	private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

