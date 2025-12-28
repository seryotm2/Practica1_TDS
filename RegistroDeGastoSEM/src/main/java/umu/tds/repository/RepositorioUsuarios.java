package umu.tds.repository;

import java.util.List;

import umu.tds.modeloNegocio.CuentaCompartida;
import umu.tds.modeloNegocio.Usuario;

public interface RepositorioUsuarios {
	
	/**
	 * Devuelve la lista de usuarios almacenados
	 * @return Lista de Usuarios.
	 */
	List<Usuario> getUsuarios();
	
	/**
	 * Devuelve la lista de cuentas compartidas almacenadas
	 * @return Lista de cuentas compartidas.
	 */
	List<CuentaCompartida> getCuentas();
	
	void guardarUsuarios(List<Usuario> usuarios);
    void guardarCuentas(List<CuentaCompartida> cuentas);
	
}
