package umu.tds.modeloNegocio.buscadores;

import java.util.Set;

import umu.tds.modeloNegocio.Gasto;

public class BuscadorConConcepto extends BuscadorCompuesto{

	private String concepto;
	
	/**
	 * Dado un objeto BucadorCategoria, crea un nuevo buscador que le añade al buscador original
	 * la funcionalidad de buscar por concepto.
	 * @param bBase El buscador base al que se le añade la funcionalidad.
	 * @param concepto subcadena que debe contener los Gastos.
	 */
	public BuscadorConConcepto(BuscadorCategoria bBase, String concepto) {
		super(bBase);
		this.concepto = concepto;
	}
	
	@Override
	public Set<Gasto> buscar() {
		Set<Gasto> res = super.buscar();
		res.addAll(super.getCategoria().getGastosConConcepto(concepto));
		return res;
	}
	
}
