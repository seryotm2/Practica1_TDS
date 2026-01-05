package umu.tds.modeloNegocio;

import java.util.List;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.AlertasEstrategia.AlertaMensual;
import umu.tds.modeloNegocio.AlertasEstrategia.AlertaSemanal;
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
	
	
	@Override
    public void notificar() {
        List<Alerta> alertasCumplidas = repositorio.getAlertas().stream()
            .filter(Alerta::evaluar)
            .collect(Collectors.toList());

        if (!alertasCumplidas.isEmpty()) {
            alertasCumplidas.forEach(alerta -> {
                alerta.setDisparada(true);
                repositorio.agregarNotificacion(alerta); 
                repositorio.borrarAlerta(alerta);        
            });
        }
    }
		
	
}
