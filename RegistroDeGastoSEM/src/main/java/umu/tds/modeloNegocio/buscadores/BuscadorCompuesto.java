package umu.tds.modeloNegocio.buscadores;

import java.util.Set;

import umu.tds.modeloNegocio.Gasto;

public abstract class BuscadorCompuesto extends BuscadorCategoria{
	
	private BuscadorCategoria base;
	
	public BuscadorCompuesto(BuscadorCategoria base) {
		this.base = base;
	}
	
	@Override
	public Set<Gasto> buscar() {
		 return base.buscar();
	}
}
