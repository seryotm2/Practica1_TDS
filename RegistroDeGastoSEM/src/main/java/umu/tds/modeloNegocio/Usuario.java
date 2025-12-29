package umu.tds.modeloNegocio;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
	    generator = ObjectIdGenerators.PropertyGenerator.class,
	    property = "nombre" 
	)
public class Usuario implements Comparable<Usuario>{
	private String nombre;
	private String email;
	
	public Usuario() {
	}
	
	public Usuario(String nombre, String email) {
		this.nombre = nombre;
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return this.nombre + " (email: " + this.email + ")\n";
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nombre, usuario.nombre)
        		&& Objects.equals(email, usuario.email);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(email, nombre);
	}

	@Override
	public int compareTo(Usuario o) {
		int comp = this.nombre.compareTo(o.nombre);
		if(comp == 0)
			return this.email.compareTo(o.email);
		return comp;
	}
}
