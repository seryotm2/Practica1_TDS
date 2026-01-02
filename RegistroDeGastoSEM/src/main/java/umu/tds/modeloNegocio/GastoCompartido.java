package umu.tds.modeloNegocio;

import java.time.LocalDate;
import java.util.Map;

public class GastoCompartido extends Gasto {
    
    //private CuentaCompartida cuenta;
    private int cuenta;
   // private Usuario pagador;
    
    private Map<String, Double> porcentajes; 

    public GastoCompartido() { super(); }

    public GastoCompartido(String concepto, double cantidad, LocalDate fecha, CuentaCompartida cuenta, Usuario pagador, Map<String, Double> porcentajes) {
    	super(cantidad, fecha);
    		this.setConcepto(concepto);

    		this.setUsuario(pagador); 

    		this.cuenta = cuenta.getId();
    		//this.pagador = pagador;
    		this.porcentajes = porcentajes;
    }
    

    public int getCuenta() { return cuenta; }
    //public Usuario getPagador() { return pagador; }
    public Map<String, Double> getPorcentajes() { return porcentajes; }

    public double getPorcentajeDe(Usuario u) {
        if (porcentajes != null && u != null && porcentajes.containsKey(u.getNombre())) {
            return porcentajes.get(u.getNombre());
        }
        return 0.0;
    }
}