package umu.tds.modeloNegocio;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Directorio {
    
    public static final String NOMBRE_PROPIETARIO = "Yo";  //esto lo dejo de momento así, ya veremos la manera de cambiar el nombre
    
    private Map<String, Usuario> usuarios;
    private static Usuario usuarioPropietario = new Usuario(NOMBRE_PROPIETARIO, ""); ;

    public Directorio() {
        this.usuarios = new HashMap<>();
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
        return true;
    }

    public Optional<Usuario> buscarUsuario(String nombre) {
        return Optional.ofNullable(usuarios.get(nombre));
    }

    /**
     * Devuelve el usuario principal (Dueño) que siempre existe.
     */
    public static Usuario getUsuarioPropietario() {
        return usuarioPropietario;
    }

    public Map<String, Usuario> getUsuarios() {
        return new HashMap<>(usuarios);
    }
}