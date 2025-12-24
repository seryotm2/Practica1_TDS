package umu.tds.modeloNegocio;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;

public class Gasto implements Comparable<Gasto>{
	
	private String concepto;
	private double cantidad;
	private LocalDate fecha;
	private Categoria categoria;
	private Usuario usuario;
	
	public Gasto(double cantidad, LocalDate fecha) {
		this.cantidad = cantidad;
		this.fecha = fecha;
		this.concepto = "";
	}
	
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	
	void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	
	public Categoria getCategoria() {
		return categoria;
	}
	

	void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * Retorna true si el gasto fue realizado entre el periodo de tiempo f1 y f2, false en otro caso.
	 * @param f1 fecha inferior del periodo. Debe ser menor o igual a f2 .
	 * @param f2 fecha superior del periodo. Debe ser mayor o igual a f1.
	 * @return true si el gasto está entre f1 y f2, false si no.
	 */
	public boolean realizadoEntre(LocalDate f1, LocalDate f2) {
		return (this.fecha.isAfter(f1) || this.fecha.isEqual(f1)) &&
				(this.fecha.isBefore(f2) || this.fecha.isEqual(f2));
	}
	
	/**
	 * Retorna true si el gasto fue realizado en esta semana. False en otro caso.
	 */
	public boolean realizadoEnEstaSemana() {
		WeekFields semanaAct = WeekFields.of(Locale.getDefault());
		
		return fecha.get(semanaAct.weekOfWeekBasedYear()) ==  LocalDate.now().get(semanaAct.weekOfWeekBasedYear()) &&
		        fecha.get(semanaAct.weekBasedYear()) == LocalDate.now().get(semanaAct.weekBasedYear());
	}
	
	/**
 	 * Retorna true si el gasto fue realizado en este mes. False en otro caso.
	 */
	public boolean realizadoEnEsteMes() {
		return YearMonth.from(fecha).equals(YearMonth.now());
	}
	
	public boolean isCantidadEntre(double li, double ls) {
		return this.cantidad >= li && this.cantidad <= ls;
	}
	
	@Override
	public String toString() {
		return "Gasto [fecha=" + fecha + ", cantidad=" + cantidad +  ", concepto=" + concepto + ", categoría="
				+ categoria + ", usuario=" + usuario + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, categoria, concepto, fecha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gasto other = (Gasto) obj;
		return Double.doubleToLongBits(cantidad) == Double.doubleToLongBits(other.cantidad)
				&& Objects.equals(categoria, other.categoria) && Objects.equals(concepto, other.concepto)
				&& Objects.equals(fecha, other.fecha);
	}

	@Override
	public int compareTo(Gasto o) {
		if(this.fecha.isAfter(o.fecha))
			return 1;
		if(this.fecha.isBefore(o.fecha))
			return -1;
		int empate = ((Double)cantidad).compareTo(o.cantidad);
		if(empate != 0)
			return empate;
		return concepto.compareTo(o.concepto);
	}

	
}
