package umu.tds.modeloNegocio.AlertasEstrategia;

import umu.tds.modeloNegocio.Alerta;
import umu.tds.modeloNegocio.Categoria;

public class AlertaMensual extends Alerta {
	
	public AlertaMensual() {
        super();
        this.tipo = new EstrategiaAlertaMensual();
    }
	
	public AlertaMensual(double limiteGasto) {
        super(limiteGasto, new EstrategiaAlertaMensual());
    }
	
	public AlertaMensual(double limiteGasto, Categoria categoria) {
        super(limiteGasto, categoria, new EstrategiaAlertaMensual());
    }

	@Override
	public String getDescripcion() {
		String cat = (getCategoria() != null) ? " en " + getCategoria().getNombreCategoria() : " Total";
        return "Mensual" + cat + " (> " + getLimiteGasto() + "€)";
	}
	
}
