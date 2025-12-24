package umu.tds.modeloNegocio.buscadores;

import java.util.Set;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.Gasto;

public class BuscadorPorCantidad extends BuscadorCategoria{
			
		private double cotaInferior;
		private double cotaSuperior;
		
		public BuscadorPorCantidad(Categoria categoria, double cotaInferior, double cotaSuperior) {
			super(categoria);
			this.cotaInferior = cotaInferior;
			this.cotaSuperior = cotaSuperior;
		}
		
		@Override
		public Set<Gasto> buscar() {
			return getCategoria().getGastosConCatidad(cotaInferior, cotaSuperior);
		}

}
