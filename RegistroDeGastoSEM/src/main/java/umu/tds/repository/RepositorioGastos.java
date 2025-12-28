package umu.tds.repository;

import java.util.Set;

import umu.tds.modeloNegocio.Categoria;
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
	 * Recupera los nombres de las categorías ya existentes en disco.
	 * @return Conjunto de nombres de categorías.
	 */
	Set<String> getIdCategorias();
	
	/**
	 * Crea un nuevo archivo para guardar los gastos de una nueva categoría
	 * @param nombreCategoria Nombre de la nueva categoría
	 */
	void registrarCategoria(String nombreCategoria);

}
