package umu.tds.modeloNegocio;

import java.time.LocalDate;
import java.util.ArrayList;
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

import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.buscadores.BuscadorCategoria;

public class LibroDeCuenta {
	
	static final public String GASTOS_GENERALES = "Gastos Generales";
	
	// Objeto singleton
	static private LibroDeCuenta instancia = null;
	
	double gastoGlobal;
	HashMap<String,Categoria> categorias;
	TreeSet<Gasto> gastosDeLaSemana, gastosDelMes;
	
	List<CuentaCompartida> cuentasCompartidas;	//por Sergio
	
	
	private LibroDeCuenta() {
		gastoGlobal = 0;
		categorias = new HashMap<>();
		gastosDeLaSemana = new TreeSet<>();
		gastosDelMes = new TreeSet<>();
		cuentasCompartidas = new ArrayList<>();
		
		// Siempre hay una catogoría por defecto que se asignará a los gastos sin categoría especificada.
		categorias.put(GASTOS_GENERALES, new Categoria(GASTOS_GENERALES));
	}
	
	static public LibroDeCuenta getInstancia() {
		if(instancia == null)
			return instancia = new LibroDeCuenta();
		return instancia;
	}

	
	
	/**
	 * Busca en la lista de cuentas compartidas la cuenta que coincida con un nombre dado.
	 * Si existe entonces regresa la cuenta.
	 * @param nombreCuenta
	 * @return Un objeto Obtional con la cuenta si la cuenta fue encontrada. Vacío en caso contrario.
	 */
	//TODO En caso de implementar otra forma de identificar las cuentas, por ejemplo, con un número
	// de secuencia, haciendo que el nombre se pueda repetir en varias cuentas, hacer que este método
	// devuelva una lista.
	
	
	//Por Sergio
    
    public void addCuentaCompartida(CuentaCompartida cuenta) {
        cuentasCompartidas.add(cuenta);
    }

    public List<CuentaCompartida> getCuentasCompartidas() {
        return Collections.unmodifiableList(cuentasCompartidas);
    }
    

    public Optional<Gasto> crearGasto(double cantidad, LocalDate fecha, Usuario usuario,
    		String concepto, String categoria) {        
        
        if(!existeCategoria(categoria))
            return Optional.empty();
        
        // Creamos el gasto y lo añadimos a la categoría
        Optional<Gasto> newGastoOpt = categorias.get(categoria).addNewGasto(cantidad, fecha, usuario, concepto);
         
        if(newGastoOpt.isPresent()) {
             Gasto newGasto = newGastoOpt.get();
             
             boolean esGastoPropio = usuario.equals(AppControlGastos.getInstancia().getUsuarioActual());

             if (esGastoPropio) {
                 gastoGlobal += newGasto.getCantidad();
                 
                 if(newGasto.realizadoEnEsteMes())
                     addIntoGastosDelMes(newGasto);
                 
                 if(newGasto.realizadoEnEstaSemana())
                     addIntoGastosDeLaSemana(newGasto);
             }
         }
         return newGastoOpt;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	public double getGastoGlobal() {
		return gastoGlobal;
	}
	
	public double getGastoSemanal() {
		updateGastosDeLaSemana();
		return gastosDeLaSemana.stream()
			.collect(Collectors.summingDouble(g-> g.getCantidad()));
	}
	
	public double getGastoMensual() {
		updateGastosDelMes();
		return gastosDelMes.stream()
			.collect(Collectors.summingDouble(g-> g.getCantidad()));
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

	/**
	 * Añade gasto al conjunto de gastos de este mes. Se debe haber comprobado
	 * que el gasto sí se realizó en la semana actual.
	 * @param gasto a añadir.
	 */
	private void addIntoGastosDelMes(Gasto gasto) {
		updateGastosDelMes();
		gastosDelMes.add(gasto);
	}

	public Optional<Gasto> crearGasto(double cantidad, LocalDate fecha, Usuario usuario,
			String concepto){
		
		return crearGasto(cantidad, fecha, usuario, concepto, GASTOS_GENERALES);
	}
	
	/**
	 * Retorna los últimos n gastos creados si los hay. La lista puede estar vacía.
	 * @param n Número de gastos solicitados.
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
		if(!existeCategoria(catGasto))
			return false;
		
		boolean resultado = catGasto.eliminarGasto(e);
		if(resultado) {
			boolean esGastoPropio = e.getUsuario().equals(AppControlGastos.getInstancia().getUsuarioActual());
			if(esGastoPropio) {
				if(gastosDeLaSemana.contains(e))
					gastosDeLaSemana.remove(e);
				if(gastosDelMes.contains(e))
					gastosDelMes.remove(e);
				gastoGlobal -= e.getCantidad();
			}
		}
		return resultado;
	}
	
	public Set<Gasto> buscar(BuscadorCategoria buscador){
		return buscador.buscar();
	}
	
	/**
	 * Cambia de categoria un gasto. Retorna true en caso de exito o si el gasto
	 * ya pertenecía a la categoría destino, false en caso de fallo. 
	 * @param g Gasto a cambiar,
	 * @param c Categoría destino.
	 * @return Retorna true en caso de exito o si el gasto ya pertenecía a la categoría
	 * destino, false en caso de fallo.
	 */
	public boolean cambiarGastoDeCategoria(Gasto g, Categoria c) {
		if(!existeCategoria(c))
			return false;
		if(g.getCategoria().equals(c))
			return true;
		g.setCategoria(c);
		return c.addGasto(g);
	}
	
	/**
	 * Cambia de categoria un gasto. Retorna true en caso de exito o si el gasto
	 * ya pertenecía a la categoría destino, false en caso de fallo. 
	 * @param g Gasto a cambiar,
	 * @param c nombre de la categoría destino.
	 * @return Retorna true en caso de exito o si el gasto ya pertenecía a la categoría
	 * destino, false en caso de fallo.
	 */
	public boolean cambiarGastoDeCategoria(Gasto g, String c) {
		if(!existeCategoria(c))
			return false;
		Categoria cat = this.categorias.get(c);
		if(g.getCategoria().equals(cat))
			return true;
		g.setCategoria(cat);
		return cat.addGasto(g);
	}
	
	public void cambiarFechaDeGasto(Gasto g, LocalDate newFecha) {
		g.setFecha(newFecha);
		if(g.realizadoEnEsteMes())
			addIntoGastosDelMes(g);
		if(g.realizadoEnEstaSemana())
			addIntoGastosDeLaSemana(g);
	}
	
	public Optional<CuentaCompartida> buscarCuentaCompartidaPorId(int id) {
        return this.cuentasCompartidas.stream()
                .filter(cc -> cc.getId() == id)
                .findFirst();
    }
	
	/**
	 * TODO Falta la recuperación de datos desde el
	 * repositorio.
	 */
	
	public void setCuentasCompartidas(List<CuentaCompartida> cuentas) {
        this.cuentasCompartidas = new ArrayList<>(cuentas);
        
        int maxId = 0;
        for (CuentaCompartida cc : cuentas) {
            if (cc.getId() > maxId) {
                maxId = cc.getId();
            }
        }
        CuentaCompartida.setContador(maxId);
    }
}
