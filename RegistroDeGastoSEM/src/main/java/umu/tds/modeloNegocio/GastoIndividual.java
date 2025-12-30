package umu.tds.modeloNegocio;

import java.time.LocalDate;

public class GastoIndividual extends Gasto {

    public GastoIndividual() { super(); }

    public GastoIndividual(String concepto, double cantidad, LocalDate fecha) {
        super(cantidad, fecha);
        this.setConcepto(concepto);
    }
}