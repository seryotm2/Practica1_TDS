package umu.tds.modeloNegocio.buscadores;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.Gasto;

public abstract class BuscadorGastos {	 	
	
	public abstract Set<Gasto> buscar(Collection<Gasto> gastos);
	
	/**
	 * Crea un nuevo buscador que le añade al buscador original
	 * la funcionalidad de buscar por intervalo de fechas.
	 * @param fInicio Fecha de donde parte la busqueda.
	 * @param fFinal Fecha hasta donde se busca.
	 * @return Un objeto BuscadorGastos 
	 */
	public BuscadorGastos addFecha(LocalDate fInicio, LocalDate fFinal) {
		return new BuscadorConFecha(this, fInicio, fFinal);
	}
	
	/**
	 * Crea un nuevo buscador que le añade al buscador original
	 * la funcionalidad de buscar por cantidades.
	 * @param cotaInferior Catidad mínima buscada. 
	 * @param cotaSuperior Catidad máxima buscada.
	 * @return Un objeto BuscadorGastos 
	 */
	public BuscadorGastos addCantidad(double cotaInferior, double cotaSuperior) {
		return new BuscadorConCantidad(this, cotaInferior, cotaSuperior);
	}
	
	
	/**
	 * Crea un nuevo buscador que le añade al buscador original
	 * la funcionalidad de buscar por una categoria.
	 * @param categoria La categoría a filtrar. 
	 * @return Un objeto BuscadorGastos 
	 */
	public BuscadorGastos addCategoria(Categoria categoria) {
		return new BuscadorConCategoria(this, categoria);
	}
	
	/**
	 * Crea un nuevo buscador que le añade al buscador original
	 * la funcionalidad de buscar por su concepto.
	 * @param concepto El texto que debe tener el gasto. 
	 * @return Un objeto BuscadorGastos 
	 */
	public BuscadorGastos addConcepto(String concepto) {
		return new BuscadorConConcepto(this, concepto);
	}
}
