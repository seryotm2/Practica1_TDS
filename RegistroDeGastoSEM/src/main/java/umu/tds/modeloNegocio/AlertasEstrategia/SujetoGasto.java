package umu.tds.modeloNegocio.AlertasEstrategia;

public interface SujetoGasto {
	void agregarObservador(ObservadorGasto obs);
	void quitarObservador(ObservadorGasto obs);
	void notificarObservadores();
}
