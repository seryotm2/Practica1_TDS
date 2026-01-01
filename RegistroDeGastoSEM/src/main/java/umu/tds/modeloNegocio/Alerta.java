package umu.tds.modeloNegocio;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import umu.tds.modeloNegocio.AlertasEstrategia.*;

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "type"
	)
	@JsonSubTypes({
	    @JsonSubTypes.Type(value = AlertaMensual.class, name = "mensual"),
	    @JsonSubTypes.Type(value = AlertaSemanal.class, name = "semanal")
	})
public abstract class Alerta{
	private double limiteGasto;
	private Categoria categoria;
	private boolean disparada;
	private LocalDate fechaCreacion;
	
	@JsonIgnore
	protected EstrategiaAlerta tipo; 
	
	public Alerta() {
        this.fechaCreacion = LocalDate.now();
        this.disparada = false;
    }
	
	public Alerta(double limiteGasto, EstrategiaAlerta tipo) {
        this.limiteGasto = limiteGasto;
        this.tipo = tipo;
        this.categoria = null;
        this.disparada = false;
        this.fechaCreacion = LocalDate.now();
    }
	
	public Alerta(double limiteGasto, Categoria categoria, EstrategiaAlerta tipo) {
        this(limiteGasto, tipo);
        this.categoria = categoria;
    }
	
	
	public double getLimiteGasto() {
		return limiteGasto;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}
	
	public EstrategiaAlerta getTipo() {
		return tipo;
	}
	
	public boolean estaDisparada() {
        return disparada;
    }
	
	public boolean evaluar() {
		disparada = tipo.seDispara(fechaCreacion, categoria, limiteGasto); 
		return disparada;
	}
	
	public abstract String getDescripcion();
	
	public void setLimiteGasto(double l) {
		this.limiteGasto = l; 
	
	}
    public void setCategoria(Categoria c) {
    	this.categoria = c;
    
    }
    public void setDisparada(boolean d) {
    	this.disparada = d;
    }
    
    public void setFechaCreacion(LocalDate d) {
    	this.fechaCreacion = d;
    }
	 
    
	
}
