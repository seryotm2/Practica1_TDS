package umu.tds.modeloNegocio.buscadores;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Gasto;

public class BuscadorConConcepto extends BuscadorCompuesto{

	private String concepto;
	
	/**
	 * Dado un objeto BucadorCategoria, crea un nuevo buscador que le añade al buscador original
	 * la funcionalidad de buscar por concepto.
	 * @param bBase El buscador base al que se le añade la funcionalidad.
	 * @param concepto subcadena que debe contener los Gastos.
	 */
	public BuscadorConConcepto(BuscadorGastos bBase, String concepto) {
		super(bBase);
		this.concepto = concepto.trim().toLowerCase();
	}
	
	@Override
	public Set<Gasto> buscar(Collection<Gasto> gastos) {
		Set<Gasto> res = super.buscar(gastos);
		res.addAll(gastos.stream()
				.filter(g-> g.getConcepto().trim().toLowerCase().equals(concepto))
				.collect(Collectors.toSet()));
		return res;
	}
	
}
