package umu.tds.modeloNegocio.buscadores;

import java.util.Collection;
import java.util.Set;

import umu.tds.modeloNegocio.Gasto;

public abstract class BuscadorCompuesto extends BuscadorGastos{
	
	private BuscadorGastos base;
	
	public BuscadorCompuesto(BuscadorGastos base) {
		this.base = base;
	}
	
	@Override
	public Set<Gasto> buscar(Collection<Gasto> gastos) {
		return base.buscar(gastos);
	}
	
}
