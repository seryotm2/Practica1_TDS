package umu.tds.modeloNegocio.AlertasEstrategia;

import umu.tds.modeloNegocio.Alerta;
import umu.tds.modeloNegocio.Categoria;

public class AlertaMensual extends Alerta {
	
	public AlertaMensual(double limiteGasto) {
        super(limiteGasto, new EstrategiaAlertaMensual());
    }
	
	public AlertaMensual(double limiteGasto, Categoria categoria) {
        super(limiteGasto, categoria, new EstrategiaAlertaMensual());
    }
	
}
