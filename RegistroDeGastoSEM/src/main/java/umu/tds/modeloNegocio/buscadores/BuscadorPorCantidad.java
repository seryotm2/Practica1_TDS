package umu.tds.modeloNegocio.buscadores;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Gasto;

public class BuscadorPorCantidad extends BuscadorGastos{
			
		private double cotaInferior;
		private double cotaSuperior;
		
		public BuscadorPorCantidad(double cotaInferior, double cotaSuperior) {
			this.cotaInferior = cotaInferior;
			this.cotaSuperior = cotaSuperior;
		}

		@Override
		public Set<Gasto> buscar(Collection<Gasto> gastos) {
			return gastos.stream()
					.filter(g-> g.isCantidadEntre(cotaInferior, cotaSuperior))
					.collect(Collectors.toSet());
		}
		
		

}
