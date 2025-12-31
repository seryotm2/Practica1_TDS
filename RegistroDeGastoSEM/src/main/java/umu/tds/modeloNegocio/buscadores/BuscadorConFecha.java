package umu.tds.modeloNegocio.buscadores;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Gasto;

public class BuscadorConFecha extends BuscadorCompuesto{
	
	private LocalDate fechaInicio;
	private LocalDate fechaLimite;
	
	/**
	 * Dado un objeto BucadorCategoria, crea un nuevo buscador que le añade al buscador original
	 * la funcionalidad de buscar por intervalo de fechas. 
	 * @param bBase El buscador base al que se le añade la funcionalidad
	 * @param fechaInicio Fecha de donde parte la busqueda.
	 * @param fechaLimite Fecha hasta donde se busca.
	 */
	public BuscadorConFecha(BuscadorGastos bBase, LocalDate fechaInicio, LocalDate fechaLimite) {
		super(bBase);
		this.fechaInicio = fechaInicio;
		this.fechaLimite = fechaLimite;
	}
	
	@Override
	public Set<Gasto> buscar(Collection<Gasto> gastos) {
		Set<Gasto> res = super.buscar(gastos);
		return res.stream()
				.filter(g-> g.realizadoEntre(fechaInicio, fechaLimite))
				.collect(Collectors.toSet());
	}

}
