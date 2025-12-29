package umu.tds.modeloNegocio;

import java.time.LocalDate;

import umu.tds.modeloNegocio.AlertasEstrategia.EstrategiaAlerta;

public class Alerta{
	private double limiteGasto;
	private EstrategiaAlerta tipo; 
	private Categoria categoria;
	private boolean disparada;
	private LocalDate fechaCreacion;
	
	public Alerta() {}
	
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
	
}
