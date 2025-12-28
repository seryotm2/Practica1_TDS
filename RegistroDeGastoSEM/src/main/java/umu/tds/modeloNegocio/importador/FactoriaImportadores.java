package umu.tds.modeloNegocio.importador;

public class FactoriaImportadores {
    
    public static IImportador getImportador(String archivo) {
        if (archivo.endsWith(".csv")) {
            return new ImportadorCSV();
        } 
        
        return null; 
    }
}