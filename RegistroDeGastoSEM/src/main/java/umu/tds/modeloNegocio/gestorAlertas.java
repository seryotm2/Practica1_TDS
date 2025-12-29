package umu.tds.modeloNegocio;

import java.util.List;

import umu.tds.modeloNegocio.AlertasEstrategia.EstrategiaAlerta;
import umu.tds.modeloNegocio.AlertasEstrategia.ObservadorGasto;
import umu.tds.repository.RepositorioAlertas;

public class gestorAlertas implements ObservadorGasto{
	
	// Objeto singleton
	static private gestorAlertas instancia = null;
		
	private RepositorioAlertas repositorio;
	
    private gestorAlertas(RepositorioAlertas rA) {
    	this.repositorio = rA;
	}
    
    
    static public gestorAlertas getInstancia(RepositorioAlertas rA) {
		if(instancia == null) {
			instancia = new gestorAlertas(rA);
		}
		return instancia;
	}
    
    static public gestorAlertas getInstancia() {
    	return instancia;
	}
    
	public List<Alerta> mostrarHistorial() {
        return repositorio.getNotificaciones();
    }
	
	public void crearAlerta(double limiteGasto, EstrategiaAlerta tipo) {
		Alerta alerta = new Alerta(limiteGasto, tipo);
        repositorio.agregarAlerta(alerta);
    }
	
	public void crearAlerta(double limiteGasto, Categoria categoria, EstrategiaAlerta tipo) {
		Alerta alerta = new Alerta(limiteGasto, categoria, tipo);
        repositorio.agregarAlerta(alerta);
    }
	
	public void eliminarAlerta(Alerta alerta) {
        repositorio.borrarAlerta(alerta);
    }
	
	public void evaluarAlertas() {
		repositorio.getAlertas().stream()
		    .filter(alerta -> alerta.evaluar() && !repositorio.getNotificaciones().contains(alerta))
		    .forEach(a -> repositorio.agregarNotificacion(a));
    }
	
	public List<Alerta> getAlertas() {
        return repositorio.getAlertas();
    }
	
	public List<Alerta> getAlertasDisparadas() {
        return repositorio.getAlertasDisparadas();
    }
	
	@Override
	public void notificar() {
		evaluarAlertas();		
	}

}
