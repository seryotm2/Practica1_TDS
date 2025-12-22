package umu.tds.modeloNegocio;

import java.time.LocalDate;
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
	
	/* De momento no necesario
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	*/
	/*todo: devolver una lista inmodificable. 
	 * 
	 * */
	public Categoria getCategoría() {
		return categoria;
	}
	

	public void setCategoría(Categoria categoria) {
		this.categoria = categoria;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		return 0;
	}
	
	
}
