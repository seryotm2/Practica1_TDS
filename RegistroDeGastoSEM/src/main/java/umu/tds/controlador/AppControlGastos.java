package umu.tds.controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import umu.tds.modeloNegocio.CuentaCompartida;
import umu.tds.modeloNegocio.Directorio;
import umu.tds.modeloNegocio.LibroDeCuenta;
import umu.tds.modeloNegocio.Usuario;
import umu.tds.repository.RepositorioGastos;
import umu.tds.modeloNegocio.importador.FactoriaImportadores;
import umu.tds.modeloNegocio.importador.IImportador;
import umu.tds.repository.RepositorioUsuarios;
import umu.tds.repository.impl.RepositorioGastosJSONImpl;
import umu.tds.repository.impl.RepositorioUsuariosJSON;
import umu.tds.modeloNegocio.Gasto;

public class AppControlGastos {

    private static AppControlGastos instancia = null;  //singleton
    private Directorio directorio;
    private LibroDeCuenta libroDeCuenta;
    private RepositorioUsuarios repositorio;
    private RepositorioGastos repoGastos;

    private AppControlGastos() {
        this.directorio = Directorio.getInstancia();
        this.libroDeCuenta = LibroDeCuenta.getInstancia();
        this.repoGastos = new RepositorioGastosJSONImpl();
        this.repositorio = new RepositorioUsuariosJSON();
        
        List<CuentaCompartida> cuentasGuardadas = repositorio.getCuentas();
        libroDeCuenta.setCuentasCompartidas(cuentasGuardadas);
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
        repositorio.guardarCuentas(libroDeCuenta.getCuentasCompartidas());
        return true;
    }
    
    /**
     * Registra un gasto dentro de una cuenta compartida.
     * pero solo afectará al saldo global del usuario si el lo pagó.
     */
    public boolean registrarGastoCompartido(CuentaCompartida cuenta, String concepto, double cantidad, LocalDate fecha, Usuario pagador) {
    	 
        
        if (!libroDeCuenta.existeCategoria(concepto)) {
            libroDeCuenta.crearCategoria(concepto);
        }
        
        Optional<Gasto> gastoOpt = libroDeCuenta.crearGasto(cantidad, fecha, pagador, concepto, concepto);
        
        if (gastoOpt.isPresent()) {
            boolean exito = cuenta.addGasto(gastoOpt.get());
            
            if (exito) {
                repositorio.guardarCuentas(libroDeCuenta.getCuentasCompartidas());
            }
            return exito;
        }
        
        return false;
       
    }
    
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
    
}