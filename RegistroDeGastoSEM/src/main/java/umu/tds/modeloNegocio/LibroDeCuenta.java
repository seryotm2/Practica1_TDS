package umu.tds.modeloNegocio;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class LibroDeCuenta {
	
	static final public String GASTOS_GENERALES = "gastosGenerales";
	
	// Objeto singleton
	static private LibroDeCuenta instancia = null;
	
	double gastoGlobal;
	HashMap<String,Categoria> categorias;
	TreeSet<Gasto> gastosDeLaSemana, gastosDelMes;
	
	private LibroDeCuenta() {
		gastoGlobal = 0;
		categorias = new HashMap<>();
		gastosDeLaSemana = new TreeSet<>();
		gastosDelMes = new TreeSet<>();
		
		// Siempre hay una catogoría por defecto que se asignará a los gastos sin categoría especificada.
		categorias.put(GASTOS_GENERALES, new Categoria(GASTOS_GENERALES));
	}
	
	static public LibroDeCuenta getInstancia() {
		if(instancia == null)
			return instancia = new LibroDeCuenta();
		return instancia;
	}

	public double getGastoGlobal() {
		return gastoGlobal;
	}

	public Set<String> getNombresCategorias() {
		return categorias.keySet();
	}

	public Set<Gasto> getGastosDeLaSemana() {
		updateGastosDeLaSemana();
		return Collections.unmodifiableSet(gastosDeLaSemana);
	}

	public Set<Gasto> getGastosDelMes() {
		updateGastosDelMes();
		return Collections.unmodifiableSet(gastosDelMes);
	}
	
	public boolean crearCategoria(String nombre) {
		if(categorias.containsKey(nombre))
			return false;
		categorias.put(nombre, new Categoria(nombre));
		return true;
	}
	
	public boolean existeCategoria(String nombre) {
		return categorias.containsKey(nombre);
	}
	
	public boolean existeCategoria(Categoria c) {
		return categorias.containsValue(c);
	}
	
	public Optional<Gasto> crearGasto(double cantidad, LocalDate fecha, Usuario usuario,
			String concepto, String categoria){		
		if(!existeCategoria(categoria))
			return Optional.empty();
		
		 Optional<Gasto> newGasto = categorias.get(categoria).addNewGasto(cantidad, fecha, usuario, concepto);
		 if(newGasto.isPresent()) {
			 //se incrementa la cantidad total de dinero gastado
			 
			 //TODO Antes de sumar el gasto hay que ver si el usuario del gasto coincide con
			 // el usuario por defecto, es decir, si el usauario del nuevo gasto es igual
			 // a, por ejemplo, "Directorio.MY_USUARIO" sumar el gasto, si no, no se suma.
			 gastoGlobal += newGasto.get().getCantidad();
			 // Se evalua si el gasto corresponde a este mes
			 if(newGasto.get().realizadoEnEsteMes())
				 addIntoGastosDelMes(newGasto.get());
			// Se evalua si el gasto corresponde a esta semana
			 if(newGasto.get().realizadoEnEstaSemana())
				 addIntoGastosDeLaSemana(newGasto.get());
		 }
		 return newGasto;
	}
	
	/**
	 * Actualiza el conjunto de los gastos de esta semana.
	 */
	private void updateGastosDeLaSemana() {
		Iterator<Gasto> it = gastosDeLaSemana.iterator();
		while(it.hasNext()) {
			var gAct = it.next();
			if(!gAct.realizadoEnEstaSemana())
				it.remove();
		}
	}
	
	/**
	 * Actualiza el conjunto de los gastos de este mes.
	 */
	private void updateGastosDelMes() {
		Iterator<Gasto> it = gastosDelMes.iterator();
		while(it.hasNext()) {
			var gAct = it.next();
			if(!gAct.realizadoEnEsteMes())
				it.remove();
		}
	}
	
	/**
	 * Añade gasto al conjunto de gastos de esta semana. Se debe haber comprobado
	 * que el gasto sí se realizó en la semana actual.
	 * @param gasto a añadir.
	 */
	private void addIntoGastosDeLaSemana(Gasto gasto) {
		updateGastosDeLaSemana();
		gastosDeLaSemana.add(gasto);		
	}

	private void addIntoGastosDelMes(Gasto gasto) {
		updateGastosDelMes();
		gastosDelMes.add(gasto);
	}

	public Optional<Gasto> crearGasto(double cantidad, LocalDate fecha, Usuario usuario,
			String concepto){
		
		return crearGasto(cantidad, fecha, usuario, concepto, GASTOS_GENERALES);
	}
	
	/*
	 * Retorna los últimos n gastos creados si los hay. La lista puede estar vacía.
	 * */
	public List<Gasto> getUltimosNGastos(int n){
		
		LinkedList<Gasto> list = new LinkedList<>();
		if(n<=0) return list;
		
		for(var cat: categorias.values()) {
			if(cat.isEmpty())
				continue;
			list.addAll(cat.getUltimosNGastos(n)); 
		}
		if(!list.isEmpty()) {
			return list.stream().sorted(Comparator.reverseOrder()).limit(n)
					.collect(Collectors.toList());
		}
		return list;
	}
	
	public boolean eliminarGasto(Gasto e) {
		var catGasto = e.getCategoria();
		if(!categorias.containsValue(catGasto))
			return false;
		boolean resultado = catGasto.eliminarGasto(e);
		if(resultado)
			//TODO Antes de restar el gasto hay que ver si el usuario del gasto coincide con
			 // el usuario por defecto, es decir, si el usauario del gasto es igual
			 // a, por ejemplo, "Directorio.MY_USUARIO" restar el gasto, si no, no se suma.
			gastoGlobal -= e.getCantidad();
		return resultado;
	}
	
	/**
	 * TODO Crear las clases para las busquedas y las modificaciones. Falta la recuperación de datos desde el
	 * repositorio.
	 */
}
