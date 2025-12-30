package umu.tds.repository;

import java.util.List;
import java.util.Set;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.CuentaCompartida;
import umu.tds.modeloNegocio.Gasto;

public interface RepositorioGastos {
	
	/**
	 * Obtiene un conjunto de gastos registrados
	 * @param categoria La categoria a la que pertenecen los Gastos.
	 * @return Conjunto de gastos
	 */
	Set<Gasto> getGastos(Categoria categoria);
	
	/**
	 * Actualiza la coleccíon de gastos de una categoría en disco.
	 * @param categoria La categoria a actualizar.
	 */
	void updateGastos(Categoria categoria);
	
	/**
	 * Elimina un gasto de la persistencia del histórico
	 * @param gasto Gasto a eliminar.
	 */
	void eliminarGasto(Gasto gasto);
	
	/**
	 * Devuelve todos los gastos del sistema independientemente de la categiría.
	 * @return Conjunto de gastos
	 */
	Set<Gasto> getHistorico();
	
	/**
	 * Añade un nuevo Gasto al historico de gastos.
	 * @param newGasto El nuevo gasto a añadir.
	 */
	void updateHistorico(Gasto newGasto);
	
	/**
	 * Recupera los nombres de las categorías ya existentes en disco.
	 * @return Conjunto de nombres de categorías.
	 */
	Set<String> getIdCategorias();
	
	/**
	 * Crea un nuevo archivo para guardar los gastos de una nueva categoría
	 * @param nombreCategoria Nombre de la nueva categoría
	 */
	void registrarCategoria(String nombreCategoria);
	
	/**
	 * Retorna una lista de cuentas compartidas guardadas en disco.
	 * @return Lista de cuentas.
	 */
	List<CuentaCompartida> getCuentas();
	
	/**
	 * Actualiza la lista de cuentas compartidas almacenadas en disco.
	 * @param lista Nueva lista a almacenar.
	 */
	void updateCuentas(List<CuentaCompartida> lista);
	
	public void addGasto(Gasto gasto); //por sergio
	

}
