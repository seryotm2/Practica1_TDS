package umu.tds.controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.CuentaCompartida;
import umu.tds.modeloNegocio.Directorio;
import umu.tds.modeloNegocio.LibroDeCuenta;
import umu.tds.modeloNegocio.Usuario;
import umu.tds.modeloNegocio.buscadores.BuscadorGastos;
import umu.tds.modeloNegocio.buscadores.BuscadorPorCantidad;
import umu.tds.modeloNegocio.buscadores.BuscadorPorCategoria;
import umu.tds.modeloNegocio.buscadores.BuscadorPorConcepto;
import umu.tds.modeloNegocio.buscadores.BuscadorPorFecha;
import umu.tds.repository.RepositorioGastos;
import umu.tds.modeloNegocio.importador.FactoriaImportadores;
import umu.tds.modeloNegocio.importador.IImportador;
import umu.tds.repository.RepositorioUsuarios;
import umu.tds.repository.impl.RepositorioGastosJSONImpl;
import umu.tds.repository.impl.RepositorioUsuariosJSON;
import umu.tds.modeloNegocio.Gasto;
import umu.tds.modeloNegocio.GastoCompartido;
import umu.tds.modeloNegocio.GastoIndividual;

public class AppControlGastos {

    private static AppControlGastos instancia = null;  //singleton
    private Directorio directorio;
    private LibroDeCuenta libroDeCuenta;
    private RepositorioUsuarios repositorioUsuarios;
    private RepositorioGastos repoGastos;

    /*
    private AppControlGastos() {
        this.directorio = Directorio.getInstancia();
        this.libroDeCuenta = LibroDeCuenta.getInstancia();
        this.repoGastos = new RepositorioGastosJSONImpl();
        this.repositorio = new RepositorioUsuariosJSON();
        
        List<CuentaCompartida> cuentasGuardadas = repositorio.getCuentas();
        libroDeCuenta.setCuentasCompartidas(cuentasGuardadas);
    }
    */
    
    private AppControlGastos() {
        this.repoGastos = new RepositorioGastosJSONImpl();
        this.repositorioUsuarios = new RepositorioUsuariosJSON();
        this.directorio = Directorio.getInstancia();
        this.libroDeCuenta = LibroDeCuenta.getInstancia();
    }

    public static AppControlGastos getInstancia() {
        if (instancia == null) {
            instancia = new AppControlGastos();
        }
        return instancia;
    }
    
    public RepositorioGastos getRepoGastos() {
    	return repoGastos;
    }
    
    public Usuario getUsuarioActual() {
        return Directorio.getUsuarioPropietario();
    }

    public boolean registrarUsuario(String nombre, String email) {
        return directorio.crearUsuario(nombre, email);
    }


    /**
     * Registra un gasto para el usuario principal.
     * Si la categoría indicada no existe, se crea automáticamente. TODO: CAMBIAR ESTO, LO DEJAREMOS COMO LISTA DESLIZANTE
     * @param concepto Descripción del gasto.
     * @param nombreCategoria Nombre de la categoría (ej: "Alimentación").
     * @param cantidad Importe del gasto.
     * @param fecha Fecha en la que se realizó.
     * @return true si el gasto se registró con éxito.
     */
    
    /*
    public boolean registrarGasto(String concepto, String nombreCategoria, double cantidad, LocalDate fecha) {
        if (cantidad < 0 || fecha == null) {
            return false;
        }
        Usuario autor = getUsuarioActual();
        if (nombreCategoria == null || nombreCategoria.isBlank()) {  
            nombreCategoria = LibroDeCuenta.GASTOS_GENERALES;
        } else {
            if (!libroDeCuenta.existeCategoria(nombreCategoria)) {	// TODO mirar si creamos una nueva o lo ponemos como una lista deslizante
                boolean creada = libroDeCuenta.crearCategoria(nombreCategoria);
                if (!creada) return false; 
            }
        }
        Optional<Gasto> gasto = libroDeCuenta.crearGasto(cantidad, fecha, autor, concepto, nombreCategoria);

        return gasto.isPresent();
    }
    */
    
    /**
     * Crea una nueva cuenta compartida.
     * @param nombre Nombre de la cuenta.
     * @param participantes Lista de usuarios involucrados.
     */
    public boolean crearCuentaCompartida(String nombre, List<Usuario> participantes) {
    	List<Usuario> listaCompleta = new ArrayList<>(participantes);
        Usuario propietario = getUsuarioActual();
        if (!listaCompleta.contains(propietario)) {
            listaCompleta.add(propietario);
        }
        CuentaCompartida nuevaCuenta = new CuentaCompartida(nombre, listaCompleta);
        
        libroDeCuenta.addCuentaCompartida(nuevaCuenta);
        repoGastos.updateCuentas(libroDeCuenta.getCuentasCompartidas());
        return true;
    }
    
    /*
    public boolean registrarGastoCompartido(CuentaCompartida cuenta, String concepto, double cantidad, LocalDate fecha, Usuario pagador) {
    	 
        
        if (!libroDeCuenta.existeCategoria(concepto)) {
            libroDeCuenta.crearCategoria(concepto);
        }
        
        Optional<Gasto> gastoOpt = libroDeCuenta.crearGasto(cantidad, fecha, pagador, concepto, concepto);
        
        if (gastoOpt.isPresent()) {
            boolean exito = cuenta.addGasto(gastoOpt.get());
            
            if (exito) {
            	repoGastos.updateCuentas(libroDeCuenta.getCuentasCompartidas());
            }
            return exito;
        }
        
        return false;
       
    }
    */
    
    public List<CuentaCompartida> getCuentasCompartidas() {
        return libroDeCuenta.getCuentasCompartidas();
    }
    
    public Optional<Usuario> obtenerUsuario(String nombre) {
        return directorio.buscarUsuario(nombre);
    }
    
    public Optional<CuentaCompartida> buscarCuenta(String nombre) {
        return libroDeCuenta.getCuentasCompartidas().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }
    
    /**
     * Registra una nueva categoría.
     * @param nombre Nombre de la categoría.
     * @return true si se creó, false si ya existía.
     */
    public boolean registrarCategoria(String nombre) {
        return libroDeCuenta.crearCategoria(nombre);
    }
    
    
    
    /**
     * Devuelve todos los gastos registrados en el histórico.
     */
    public List<Gasto> obtenerTodosLosGastos() {
        // Convertimos el Set a List para que sea fácil de usar en la vista
        return new ArrayList<>(repoGastos.getHistorico());
    }

    /*
    public double obtenerGastoTotal() {
        return repoGastos.getHistorico().stream()
                .mapToDouble(Gasto::getCantidad)
                .sum();
    }
    */
    
    
    
    
    public boolean importarGastos(String rutaFichero) {
        IImportador importador = FactoriaImportadores.getImportador(rutaFichero);
        
        if (importador == null) {
            System.err.println("Formato de archivo no soportado.");
            return false;
        }
        
        List<Gasto> gastos = importador.importarGastos(rutaFichero);
        
        if (!gastos.isEmpty()) {
             return true;
        }
        return false;
    }
    
    /*
    public double obtenerGastoMensual() {
        LocalDate hoy = LocalDate.now();
        return repoGastos.getHistorico().stream()
                .filter(g -> g.getFecha().getMonth() == hoy.getMonth() && 
                             g.getFecha().getYear() == hoy.getYear())
                .mapToDouble(Gasto::getCantidad)
                .sum();
    }

    public double obtenerGastoSemanal() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate finSemana = hoy.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        return repoGastos.getHistorico().stream()
                .filter(g -> !g.getFecha().isBefore(inicioSemana) && !g.getFecha().isAfter(finSemana))
                .mapToDouble(Gasto::getCantidad)
                .sum();
    }
    */
    
    public boolean eliminarGasto(Gasto g) {
    	repoGastos.eliminarGasto(g);
    	return true;
        
    }

    public boolean eliminarCuentaCompartida(CuentaCompartida c) {
        if (c == null) return false;
        
        List<CuentaCompartida> listaOriginal = libroDeCuenta.getCuentasCompartidas();
        
        List<CuentaCompartida> listaModificable = new ArrayList<>(listaOriginal);
        
        if (listaModificable.remove(c)) {
            repoGastos.updateCuentas(listaModificable); 
            return true;
        }
        return false;
    }

    public boolean eliminarUsuario(Usuario u) {
        if (Directorio.getInstancia().eliminarUsuario(u)) {
             return true;
        }
        return false;
    }
    
    
    
    
    
    
    
    
   
    

    public double obtenerGastoTotal() {
        return repoGastos.getHistorico().stream().mapToDouble(this::calcularMiParte).sum();
    }

    public double obtenerGastoMensual() {
        return repoGastos.getHistorico().stream()
        		.filter(Gasto::realizadoEnEsteMes)
            	.mapToDouble(this::calcularMiParte)
            	.sum();
    }

    public double obtenerGastoSemanal() {
        return repoGastos.getHistorico().stream()
                .filter(Gasto::realizadoEnEstaSemana)
                .mapToDouble(this::calcularMiParte)
                .sum();
    }
    
    
    
    
    public boolean registrarGasto(String concepto, String categoria, double cantidad, LocalDate fecha) {
        try {
            GastoIndividual g = new GastoIndividual(concepto, cantidad, fecha);
            repoGastos.addGasto(g);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean registrarGastoCompartido(CuentaCompartida cuenta, String concepto, double cantidad, 
                                            LocalDate fecha, Usuario pagador, Map<String, Double> porcentajes) {
        try {
            if (cuenta == null || pagador == null || porcentajes == null) return false;
            
            GastoCompartido g = new GastoCompartido(concepto, cantidad, fecha, cuenta, pagador, porcentajes);
            
            repoGastos.addGasto(g); 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private double calcularMiParte(Gasto g) {
        Usuario yo = getUsuarioActual();
        if (yo == null) return 0.0;

        if (g instanceof GastoIndividual) {
            return g.getCantidad();
        } else if (g instanceof GastoCompartido) {
            GastoCompartido gc = (GastoCompartido) g;
            double miPorcentaje = gc.getPorcentajeDe(yo);
            
            return gc.getCantidad() * (miPorcentaje / 100.0); 
        }
        return 0.0;
    }
    

    public Set<Gasto> buscarGasto(BuscadorGastos buscador){
    	return libroDeCuenta.buscarGasto(buscador);
    }
    
    public BuscadorGastos crearBuscadorFecha(LocalDate fInicio, LocalDate fFinal) {
    	return new BuscadorPorFecha(fInicio, fFinal);
    }
    
    public BuscadorGastos crearBuscadorConcepto(String concepto) {
    	return new BuscadorPorConcepto(concepto);
    }
    
    public BuscadorGastos crearBuscadorCantidad(double cotaInferior, double cotaSuperior) {
    	return new BuscadorPorCantidad(cotaInferior, cotaSuperior);
    }
    
    public BuscadorGastos crearBuscadorCategoria(Categoria categoria) {
    	return new BuscadorPorCategoria(categoria);
    }
    
    /**
	 * Crea un nuevo buscador que le añade al buscador original bBase
	 * la funcionalidad de buscar por intervalo de fechas.
	 * @param bBase El buscador base al que se le añade la funcionalidad.
	 * @param fInicio Fecha de donde parte la busqueda.
	 * @param fFinal Fecha hasta donde se busca.
	 * @return Un objeto BuscadorGastos 
	 */
	public BuscadorGastos BuscadorAddFecha(BuscadorGastos bBase, LocalDate fInicio, LocalDate fFinal) {
		return bBase.addFecha(fInicio, fFinal);
	}
	
	/**
	 * Crea un nuevo buscador que le añade al buscador original bBase
	 * la funcionalidad de buscar por cantidades.
	 * @param bBase El buscador base al que se le añade la funcionalidad.
	 * @param cotaInferior Catidad mínima buscada. 
	 * @param cotaSuperior Catidad máxima buscada.
	 * @return Un objeto BuscadorGastos 
	 */
	public BuscadorGastos BuscadorAddCantidad(BuscadorGastos bBase, double cotaInferior, double cotaSuperior) {
		return bBase.addCantidad(cotaInferior, cotaSuperior);
	}
	
	
	/**
	 * Crea un nuevo buscador que le añade al buscador original bBase
	 * la funcionalidad de buscar por una categoria.
	 * @param bBase El buscador base al que se le añade la funcionalidad.
	 * @param categoria La categoría a filtrar. 
	 * @return Un objeto BuscadorGastos 
	 */
	public BuscadorGastos BuscadorAddCategoria(BuscadorGastos bBase, Categoria categoria) {
		return bBase.addCategoria(categoria);
	}
	
	/**
	 * Crea un nuevo buscador que le añade al buscador original bBase
	 * la funcionalidad de buscar por su concepto.
	 * @param bBase El buscador base al que se le añade la funcionalidad.
	 * @param concepto El texto que debe tener el gasto. 
	 * @return Un objeto BuscadorGastos 
	 */
	public BuscadorGastos BuscadorAddConcepto(BuscadorGastos bBase, String concepto) {
		return bBase.addConcepto(concepto);
	}
    

    
}