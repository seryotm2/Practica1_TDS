package umu.tds.modeloNegocio;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class Categoria {
	
	private String nomCategoria;
	private TreeSet<Gasto> gastos;
	private double gastoTotal;
	
	public Categoria(String nombre) {
		nomCategoria = nombre;
		gastos = new TreeSet<Gasto>();
		gastoTotal = 0;
	}

	public String getNombreCategoria() {
		return nomCategoria;
	}

	public Set<Gasto> getGastos() {
		return Collections.unmodifiableSet(gastos);
	}

	public double getGastoTotal() {
		return gastoTotal;
	}
	
	/**
	 * Crea y añade un nuevo gasto a esta categoría. Si tuvo éxito retorna un Optional con el gasto creado, si no devuelve
	 * un Optional vacío.
	 * @param cantidad Cantidad del gasto
	 * @param fecha Fecha del gasto
	 * @param usuario Persona que realizó el gasto
	 * @param concepto Una descripción para añadir al gasto 
	 * @return Un objeto Optional con el nuevo gasto añadido a la categoría o un objeto Optional vacío si el elemento
	 * ya se encontraba en la categoría.
	 */
	public Optional<Gasto> addNewGasto(double cantidad, LocalDate fecha, Usuario usuario, String concepto) {
		Gasto newGasto = new Gasto(cantidad, fecha);
		newGasto.setUsuario(usuario);
	    newGasto.setConcepto(concepto);
		newGasto.setCategoría(this);
		
		if(gastos.add(newGasto))
			return Optional.of(newGasto);
		
		return Optional.empty();		
	}
}
