package umu.tds.modeloNegocio.buscadores;

import java.util.Set;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.Gasto;

public class BuscadorPorConcepto extends BuscadorCategoria{

	private String concepto;
	
	public BuscadorPorConcepto(Categoria categoria, String concepto) {
		super(categoria);
		this.concepto = concepto;
	}
	
	@Override
	public Set<Gasto> buscar() {
		return getCategoria().getGastosConConcepto(concepto);
	}

}
