package umu.tds.modeloNegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import umu.tds.repository.RepositorioUsuarios;
import umu.tds.repository.impl.RepositorioUsuariosJSON;

public class Directorio {
    
    public static final String NOMBRE_PROPIETARIO = "Yo";  //esto lo dejo de momento así, ya veremos la manera de cambiar el nombre
    
    private RepositorioUsuarios repositorio;
    private Map<String, Usuario> usuarios;
    private static Usuario usuarioPropietario = new Usuario(NOMBRE_PROPIETARIO, ""); ;

    public Directorio() {
        this.usuarios = new HashMap<>();
        this.repositorio = new RepositorioUsuariosJSON();
        
        List<Usuario> listaCargada = repositorio.getUsuarios();
        for(Usuario u : listaCargada) {
            this.usuarios.put(u.getNombre(), u);
        }
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
}