package umu.tds.modeloNegocio.buscadores;

import java.time.LocalDate;
import java.util.Set;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.Gasto;

public class BuscadorPorFecha extends BuscadorCategoria{
	
	private LocalDate fechaInicio;
	private LocalDate fechaLimite;

	public BuscadorPorFecha(Categoria categoria, LocalDate fechaInicio, LocalDate fechaLimite) {
		super(categoria);
		this.fechaInicio = fechaInicio;
		this.fechaLimite = fechaLimite;
	}
	
	@Override
	public Set<Gasto> buscar() {
		return getCategoria().getGastosEnPeriodo(fechaInicio, fechaLimite);
	}
	
}
