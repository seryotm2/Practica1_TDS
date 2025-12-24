package umu.tds.modeloNegocio.buscadores;

import java.util.Set;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.Gasto;

public abstract class BuscadorCategoria {
	 
	private Categoria categoria;
	
	protected BuscadorCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	// Constructor para los decoradores.
	protected BuscadorCategoria() {}
	
	public Categoria getCategoria() {
		return categoria;
	}

	public abstract Set<Gasto> buscar();
	
}
