package umu.tds.modeloNegocio.buscadores;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Gasto;

public class BuscadorPorConcepto extends BuscadorGastos{

	private String concepto;
	
	public BuscadorPorConcepto(String concepto) {
		this.concepto = concepto.trim().toLowerCase();
	}

	@Override
	public Set<Gasto> buscar(Collection<Gasto> gastos) {
		return gastos.stream()
				.filter(g-> g.getConcepto().trim().toLowerCase().contains(concepto))
				.collect(Collectors.toSet());
	}
	
	

}
