package umu.tds.modeloNegocio.buscadores;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.Gasto;

public class BuscadorConCategoria extends BuscadorCompuesto{
	
	private Categoria categoria;
	
	/**
	 * Dado un objeto BucadorCategoria, crea un nuevo buscador que le añade al buscador original
	 * la funcionalidad de buscar por una categoria.
	 * @param categoria La categoría a filtrar. 
	 */
	public BuscadorConCategoria(BuscadorGastos base, Categoria categoria) {
		super(base);
		this.categoria = categoria;
	}
	
	@Override
	public Set<Gasto> buscar(Collection<Gasto> gastos) {
		Set<Gasto> res = super.buscar(gastos);
		return res.stream()
				.filter(g-> g.getCategoria().equals(categoria))
				.collect(Collectors.toSet());
	}

}
