package umu.tds.modeloNegocio;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import umu.tds.controlador.AppControlGastos;

@JsonIdentityInfo(
	    generator = ObjectIdGenerators.PropertyGenerator.class,
	    property = "nombreCategoria"
	)
public class Categoria {
	@JsonProperty("Nombre")
	private String nombreCategoria;
	
	@JsonProperty("ListadoDeGastos")
	private TreeSet<Gasto> gastos;
	
	
	private double gastoTotal;
	
	public Categoria() {} 
	
	public Categoria(String nombre) {
		nombreCategoria = nombre;
		gastos = new TreeSet<Gasto>();
		gastoTotal = 0;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public Set<Gasto> getGastos() {
		return Collections.unmodifiableSet(gastos);
	}

	@JsonIgnore
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
		newGasto.setCategoria(this);
		
		if(gastos.add(newGasto)) {
			boolean esGastoPropio = usuario.equals(AppControlGastos.getInstancia().getUsuarioActual());
			if(esGastoPropio) 
				gastoTotal += newGasto.getCantidad();
			return Optional.of(newGasto);
		}		
		return Optional.empty();		
	}
	
	public boolean addGasto(Gasto g) {
		if(!g.getCategoria().equals(this))
			return false;
		return gastos.add(g);
	}

	public int numGastos() {
		return gastos.size();
	}
	
	/**
	 * @return True si la categoría está vacía.
	 */
	public boolean isEmpty() {
		return gastos.isEmpty();
	}
	
	
	/**
	 * Retorna un objeto set con los gastos realizados entre el periodo de tiempo f1 y f2.
	 * @param f1 fecha inferior del periodo. Debe ser menor o igual a f2 .
	 * @param f2 fecha superior del periodo. Debe ser mayor o igual a f1.
	 * @return un objeto set con los gastos realizados entre el periodo de tiempo f1 y f2.
	 */
	public Set<Gasto> getGastosEnPeriodo(LocalDate f1, LocalDate f2){
		return gastos.stream()
				.filter(g->g.realizadoEntre(f1, f2))
				.collect(Collectors.toSet());
	}
	
	/**
	 * Retorna un objeto Set con los gastos que contengan en su concepto la subcadena subconcepto.
	 * @param subconcepto subcadena que debe contener el Gasto.
	 * @return un objeto set con los gastos que contengan la subcadena subconcepto.
	 */
	public Set<Gasto> getGastosConConcepto(String subconcepto){
		return gastos.stream()
				.filter(g->g.getConcepto().toLowerCase().contains(subconcepto.toLowerCase()))
				.collect(Collectors.toSet());
	}
	
	public Set<Gasto> getGastosConCatidad(double li, double ls){
		return gastos.stream()
				.filter(g->g.isCantidadEntre(li, ls))
				.collect(Collectors.toSet());
	}
	
	/*
	 * Retorna Una lista con los últimos n gastos introducidos en esta categoria si los hay.
	 * La lista puede estar vacía.
	 * */
	public List<Gasto> getUltimosNGastos(int n){
		LinkedList<Gasto> list = new LinkedList<>();
		Iterator<Gasto> it = gastos.descendingIterator();
		for(int i=0; it.hasNext() && i < gastos.size() && i < n; i++) {
			list.addLast(it.next());
		}
		return list;
	}
	
	public boolean eliminarGasto(Gasto e) {
		boolean resultado = gastos.remove(e);
		if(resultado) {
			boolean esGastoPropio = e.getUsuario().equals(AppControlGastos.getInstancia().getUsuarioActual());
			if(esGastoPropio)
				this.gastoTotal -= e.getCantidad();
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
	
}
