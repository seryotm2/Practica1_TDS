package umu.tds.modeloNegocio;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import umu.tds.controlador.AppControlGastos;


public class Categoria {

	private String nombreCategoria;
	private TreeSet<Gasto> gastos;
	//private double gastoTotal;
	private boolean cargado = false;	// atributo para saber si la categoría ha recuperado datos de disco.
	
	public Categoria() {
		this.gastos = new TreeSet<>();
	} 
	
	public Categoria(String nombre) {
		nombreCategoria = nombre;
		gastos = new TreeSet<Gasto>();
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	@JsonIgnore
	public Set<Gasto> getGastos() {
		if(!this.cargado)
			recuperarEstado();
		return Collections.unmodifiableSet(gastos);
	}

	@JsonIgnore
	public double getGastoTotal() {
		if(!this.cargado)
			recuperarEstado();
		return this.gastos.stream()
				.filter(g-> g.getUsuario().equals(Directorio.getUsuarioPropietario()))
				.collect(Collectors.summingDouble(Gasto::getCantidad));
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
		Gasto newGasto = new GastoIndividual(concepto, cantidad, fecha);
		newGasto.setUsuario(usuario);
		newGasto.setCategoria(this);
		
		if(!cargado) {recuperarEstado();}
		
		if(gastos.add(newGasto)) {			
			AppControlGastos.getRepoGastos().updateGastos(this);
			return Optional.of(newGasto);
		}		
		return Optional.empty();		
	}
	
	public Optional<Gasto> addNewGastoCompartido(CuentaCompartida cuenta, double cantidad, LocalDate fecha, 
			Usuario usuario, String concepto, Map<String, Double> porcentajes){
		
		Gasto newGasto = new GastoCompartido(concepto, cantidad, fecha, cuenta, usuario, porcentajes);
		newGasto.setCategoria(this);
		
		if(!cargado) {recuperarEstado();}
		
		if(gastos.add(newGasto)) {
			AppControlGastos.getRepoGastos().updateGastos(this);
			return Optional.of(newGasto);
		}		
		return Optional.empty();
	}
	
	public boolean addGasto(Gasto g) {
		if(!g.getCategoria().equals(this))
			return false;
		if(!cargado) {recuperarEstado();}
		boolean resultado = gastos.add(g);
		if(resultado)
			AppControlGastos.getRepoGastos().updateGastos(this);
		return resultado;
	}

	public int numGastos() {
		if(!cargado) {recuperarEstado();}
		return gastos.size();
	}
	
	/**
	 * @return True si la categoría está vacía.
	 */
	@JsonIgnore
	public boolean isEmpty() {
		if(!cargado) {recuperarEstado();}
		return gastos.isEmpty();
	}
	
		
	public boolean eliminarGasto(Gasto e) {
		if(!cargado) {recuperarEstado();}
		boolean resultado = gastos.remove(e);
		if(resultado) {
			AppControlGastos.getRepoGastos().updateGastos(this);
		}
		return resultado;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(nombreCategoria);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		return Objects.equals(nombreCategoria, other.nombreCategoria);
	}
	
	private void recuperarEstado() {
		this.gastos.addAll(AppControlGastos.getRepoGastos().getGastos(this));
		this.cargado = true;
	}
	
}
