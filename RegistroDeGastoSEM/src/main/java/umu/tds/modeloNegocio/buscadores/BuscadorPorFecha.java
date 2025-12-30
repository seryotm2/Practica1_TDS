package umu.tds.modeloNegocio.buscadores;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Gasto;

public class BuscadorPorFecha extends BuscadorGastos{
	
	private LocalDate fechaInicio;
	private LocalDate fechaLimite;

	public BuscadorPorFecha(LocalDate fechaInicio, LocalDate fechaLimite) {
		this.fechaInicio = fechaInicio;
		this.fechaLimite = fechaLimite;
	}

	@Override
	public Set<Gasto> buscar(Collection<Gasto> gastos) {
		return gastos.stream()
				.filter(g-> g.realizadoEntre(fechaInicio, fechaLimite))
				.collect(Collectors.toSet());
	}
	
	
	
}
