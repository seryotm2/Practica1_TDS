package umu.tds.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.modeloNegocio.Alerta;
import umu.tds.repository.RepositorioAlertas;
//Por hacer
public class RepositorioAlertasJSONImpl implements RepositorioAlertas{

    private List<Alerta> alertas;

    public RepositorioAlertasJSONImpl() {
        this.alertas = new ArrayList<>();
    }

    public List<Alerta> getAlertas() {
        return new ArrayList<>(alertas);
    }

    public List<Alerta> getAlertasDisparadas() {
        return alertas.stream()
                .filter(Alerta::estaDisparada)
                .collect(Collectors.toList());
    }

    public void borrarAlerta(Alerta alerta) {
        alertas.remove(alerta);
    }

    public void agregarAlerta(Alerta alerta) {
        alertas.add(alerta);
    }
}
