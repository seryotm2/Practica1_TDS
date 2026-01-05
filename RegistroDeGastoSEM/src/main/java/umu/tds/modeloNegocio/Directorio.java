package umu.tds.modeloNegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import umu.tds.controlador.AppControlGastos;
import umu.tds.repository.RepositorioUsuarios;

public class Directorio {
	
	private static Directorio instancia = null;	//Singleton
    
    public static final String NOMBRE_PROPIETARIO = "Yo";
    
    private RepositorioUsuarios repositorio;
    private Map<String, Usuario> usuarios;
    private static Usuario usuarioPropietario = new Usuario(NOMBRE_PROPIETARIO, ""); ;

    private Directorio() {
        this.usuarios = new HashMap<>();
        this.repositorio = AppControlGastos.getRepoUsuarios();
        
        List<Usuario> listaCargada = repositorio.getUsuarios();
        for(Usuario u : listaCargada) {
            this.usuarios.put(u.getNombre(), u);
        }
        if(!usuarios.containsKey(NOMBRE_PROPIETARIO)) {
        	usuarios.put(NOMBRE_PROPIETARIO, usuarioPropietario);
        	repositorio.guardarUsuarios(usuarios.values());
        }
    }
    
    /**
     * Retorna una instancia de Directorio. Sucesivas llamadas a esté método
     * retornan el mismo objeto. Solo se crea en la primeta llamada.
     * @return Un objeto Directorio.
     */
    public static Directorio getInstancia() {
    	if(instancia==null)
    		return instancia = new Directorio();
    	return instancia;
    }

    /**
     * Registra un nuevo usuario secundario (para gastos compartidos).
     * @param nombre Nombre del usuario.
     * @param email Email del usuario.
     * @return true si se creó correctamente, false si ya existía.
     */
    public boolean crearUsuario(String nombre, String email) {
        if (usuarios.containsKey(nombre)) {
            return false;
        }
        Usuario nuevoUsuario = new Usuario(nombre, email);
        usuarios.put(nombre, nuevoUsuario);
        repositorio.guardarUsuarios(new ArrayList<>(usuarios.values()));
        return true;
    }

    public Optional<Usuario> buscarUsuario(String nombre) {
        return Optional.ofNullable(usuarios.get(nombre));
    }

    /**
     * Devuelve el usuario principal 
     */
    public static Usuario getUsuarioPropietario() {
        return usuarioPropietario;
    }

    public Map<String, Usuario> getUsuarios() {
        return new HashMap<>(usuarios);
    }
    
    public boolean eliminarUsuario(Usuario u) {
        if (usuarios.containsKey(u.getNombre())) {
            usuarios.remove(u.getNombre());
            repositorio.guardarUsuarios(new ArrayList<>(usuarios.values()));
            return true;
        }
        return false;
    }
}