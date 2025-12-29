package umu.tds.modeloNegocio.AlertasEstrategia;

import java.time.LocalDate;

import umu.tds.modeloNegocio.Categoria;

public interface EstrategiaAlerta {
    boolean seDispara(LocalDate fechaCreacion, Categoria categoria, double limiteGasto);
}