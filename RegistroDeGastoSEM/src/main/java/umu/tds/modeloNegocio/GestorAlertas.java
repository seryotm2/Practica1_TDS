package umu.tds.modeloNegocio;

import java.util.List;

import umu.tds.modeloNegocio.AlertasEstrategia.AlertaMensual;
import umu.tds.modeloNegocio.AlertasEstrategia.AlertaSemanal;
import umu.tds.modeloNegocio.AlertasEstrategia.EstrategiaAlerta;
import umu.tds.modeloNegocio.AlertasEstrategia.ObservadorGasto;
import umu.tds.repository.RepositorioAlertas;

public class GestorAlertas implements ObservadorGasto{
	
	// Objeto singleton
	static private GestorAlertas instancia = null;
		
	private RepositorioAlertas repositorio;
	
    private GestorAlertas(RepositorioAlertas rA) {
    	this.repositorio = rA;
    	LibroDeCuenta.getInstancia().agregarObservador(this);
	}
    
    private GestorAlertas() {
        this.repositorio = new umu.tds.repository.impl.RepositorioAlertasJSONImpl();
        umu.tds.modeloNegocio.LibroDeCuenta.getInstancia().agregarObservador(this);
    }
    
    /*
    static public GestorAlertas getInstancia(RepositorioAlertas rA) {
		if(instancia == null) {
			instancia = new GestorAlertas(rA);
		}
		return instancia;
	}
	*/
    
    public static GestorAlertas getInstancia() {
        if (instancia == null) {
            instancia = new GestorAlertas(); 
        }
        return instancia;
    }
    
    
    
	public List<Alerta> getNotificaciones() {
        return repositorio.getNotificaciones();
    }
	
	public boolean crearAlerta(String tipoAlerta, double limiteGasto, Categoria categoria) {
		Alerta nuevaAlerta;
		
		if (tipoAlerta.equalsIgnoreCase("Mensual")) {
			nuevaAlerta = new AlertaMensual(limiteGasto, categoria);
		} else if (tipoAlerta.equalsIgnoreCase("Semanal")) {
			nuevaAlerta = new AlertaSemanal(limiteGasto, categoria);
		} else {
			return false; 
		}
		
		repositorio.agregarAlerta(nuevaAlerta);
		return true;
	}
	
	public boolean crearAlerta(String tipoAlerta, double limiteGasto) {
		return crearAlerta(tipoAlerta, limiteGasto, null);
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
	
	public void borrarNotificacion(Alerta alerta) {
        repositorio.borrarNotificacion(alerta);
    }
	
	public void notificar() {
        List<Alerta> alertas = repositorio.getAlertas();
        
        List<Alerta> notificacionesExistentes = repositorio.getNotificaciones();

        alertas.stream()
            .filter(alerta -> alerta.evaluar()) 
            .filter(alerta -> !notificacionesExistentes.contains(alerta)) 
            .forEach(alerta -> {
                alerta.setDisparada(true); 
                repositorio.agregarNotificacion(alerta);
            });
    }

	public List<Alerta> getNuevasNotificaciones() {
        return repositorio.getNotificaciones();
    }
		
	
}
