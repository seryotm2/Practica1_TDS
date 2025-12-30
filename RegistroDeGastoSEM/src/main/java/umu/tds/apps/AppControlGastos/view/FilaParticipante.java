package umu.tds.apps.AppControlGastos.view;

import javafx.beans.property.*;
import umu.tds.modeloNegocio.Usuario;

public class FilaParticipante {
    private final Usuario usuario;
    private final StringProperty nombre;
    private final DoubleProperty porcentaje;

    public FilaParticipante(Usuario usuario, double porcentajeInicial) {
        this.usuario = usuario;
        this.nombre = new SimpleStringProperty(usuario.getNombre());
        this.porcentaje = new SimpleDoubleProperty(porcentajeInicial);
    }

    public Usuario getUsuario() { return usuario; }
    
    public String getNombre() { return nombre.get(); }
    public StringProperty nombreProperty() { return nombre; }

    public double getPorcentaje() { return porcentaje.get(); }
    public void setPorcentaje(double p) { this.porcentaje.set(p); }
    public DoubleProperty porcentajeProperty() { return porcentaje; }
}