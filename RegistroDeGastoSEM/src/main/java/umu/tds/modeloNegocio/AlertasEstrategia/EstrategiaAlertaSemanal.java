package umu.tds.modeloNegocio.AlertasEstrategia;

import java.time.LocalDate;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.LibroDeCuenta;

public class EstrategiaAlertaSemanal implements EstrategiaAlerta {
	public boolean seDispara(LocalDate fechaCreacion, Categoria categoria, double limiteGasto){
		if(categoria != null) {
			return LibroDeCuenta.getInstancia().getGastosDeLaSemana().stream()
					.filter(a -> a.getCategoria().getNombreCategoria().equals(categoria.getNombreCategoria()))
					.collect(Collectors.summingDouble(g-> g.getCantidad()))
					> limiteGasto;
		}
		return LibroDeCuenta.getInstancia().getGastoSemanal() > limiteGasto;
	}
}