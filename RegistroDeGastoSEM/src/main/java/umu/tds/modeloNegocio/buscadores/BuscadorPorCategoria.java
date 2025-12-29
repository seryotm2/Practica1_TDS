package umu.tds.modeloNegocio.buscadores;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.Gasto;

public class BuscadorPorCategoria extends BuscadorGastos{
	
	private Categoria categoria;
	
	public BuscadorPorCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public Set<Gasto> buscar(Collection<Gasto> gastos) {
		return gastos.stream()
				.filter(g-> g.getCategoria().equals(categoria))
				.collect(Collectors.toSet());
	}
	

}
