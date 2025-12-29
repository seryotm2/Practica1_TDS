package umu.tds.modeloNegocio.buscadores;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Gasto;

public class BuscadorConCantidad extends BuscadorCompuesto{
	
	private double cotaInferior;
	private double cotaSuperior;
	
	/**
	 * Dado un objeto BucadorCategoria, crea un nuevo buscador que le añade al buscador original
	 * la funcionalidad de buscar por cantidades.
	 * @param bBase El buscador base al que se le añade la funcionalidad
	 * @param cotaInferior Catidad mínima buscada. 
	 * @param cotaSuperior Catidad máxima buscada.
	 */
	public BuscadorConCantidad(BuscadorGastos bBase, double cotaInferior, double cotaSuperior) {
		super(bBase);
		this.cotaInferior = cotaInferior;
		this.cotaSuperior = cotaSuperior;
	}
	
	@Override
	public Set<Gasto> buscar(Collection<Gasto> gastos) {
		Set<Gasto> res = super.buscar(gastos);
		res.addAll(gastos.stream()
				.filter(g-> g.isCantidadEntre(cotaInferior, cotaSuperior))
				.collect(Collectors.toSet()));
		return res;
	}

}
