package umu.tds.modeloNegocio.importador;

import java.util.List;
import umu.tds.modeloNegocio.Gasto;

public interface IImportador {
    /**
     * Procesa un archivo externo y lo convierte en una lista de objetos Gasto del dominio.
     * @param rutaFichero Ruta absoluta o relativa del fichero a importar.
     * @return Lista de gastos generados.
     */
    List<Gasto> importarGastos(String rutaFichero);
}