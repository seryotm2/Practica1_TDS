package umu.tds.modeloNegocio.AlertasEstrategia;

import umu.tds.modeloNegocio.Alerta;
import umu.tds.modeloNegocio.Categoria;

public class AlertaSemanal extends Alerta {
	
	public AlertaSemanal() {
        super();
        this.tipo = new EstrategiaAlertaSemanal();
    }
	
	public AlertaSemanal(double limiteGasto) {
        super(limiteGasto, new EstrategiaAlertaSemanal());
    }
	
	public AlertaSemanal(double limiteGasto, Categoria categoria) {
        super(limiteGasto, categoria, new EstrategiaAlertaSemanal());
    }

	@Override
	public String getDescripcion() {
		String cat = (getCategoria() != null) ? " en " + getCategoria().getNombreCategoria() : " Total";
        return "Semanal" + cat + " (> " + getLimiteGasto() + "€)";
	}
	
}
