package umu.tds.repository;

import java.util.List;

public interface RepositorioAlertas {
	
	/**
	 * Devuelve la lista de alertas almacenadas
	 * @return lista de alertas
	 */
	List<Alerta> getAlertas();
	
	/**
	 * Devuelve la lista de alertas disparadas
	 * @return lista de alertas
	 */
	List<Alerta> getAlertasDisparadas();
	
	/**
	 * Elimina una de las almacenadas
	 */
	void borrarAlerta(Alerta);
}
