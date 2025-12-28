package umu.tds.modeloNegocio.importador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Directorio;
import umu.tds.modeloNegocio.Gasto;
import umu.tds.modeloNegocio.LibroDeCuenta;
import umu.tds.modeloNegocio.Usuario;

public class ImportadorCSV implements IImportador {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");

    @Override
    public List<Gasto> importarGastos(String rutaFichero) {
        List<Gasto> gastosImportados = new ArrayList<>();
        
        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaFichero));
            
            boolean esCabecera = true;

            for (String linea : lineas) {
                if (esCabecera) {
                    esCabecera = false;
                    continue; 
                }
                String[] tokens = linea.split(",");
                
                if (tokens.length < 7) continue; 

                String dateStr = tokens[0].trim();
                String account = tokens[1].trim();
                String category = tokens[2].trim();
                String subcategory = tokens[3].trim();
                String note = tokens[4].trim();
                String payer = tokens[5].trim();
                String amountStr = tokens[6].trim();

                try {
                    LocalDateTime ldt = LocalDateTime.parse(dateStr, FORMATO_FECHA);
                    LocalDate fecha = ldt.toLocalDate();

                    double cantidad = Double.parseDouble(amountStr);

                    Usuario usuarioGasto;
                    if (payer.equalsIgnoreCase("Me")) {
                        usuarioGasto = Directorio.getUsuarioPropietario();
                    } else {
                        Optional<Usuario> amigoOpt = AppControlGastos.getInstancia().obtenerUsuario(payer);
                        usuarioGasto = amigoOpt.orElse(Directorio.getUsuarioPropietario());
                    }

                    if (!LibroDeCuenta.getInstancia().existeCategoria(category)) {
                        LibroDeCuenta.getInstancia().crearCategoria(category);
                    }

                    String conceptoFinal = note;
                    if (!subcategory.isEmpty()) {
                        conceptoFinal += " (" + subcategory + ")";
                    }
                    if (!account.equalsIgnoreCase("Personal")) {
                        conceptoFinal = "[" + account + "] " + conceptoFinal;
                    }

                    
                    Optional<Gasto> nuevoGasto = LibroDeCuenta.getInstancia().crearGasto(
                        cantidad, fecha, usuarioGasto, conceptoFinal, category
                    );
                    
                    nuevoGasto.ifPresent(gastosImportados::add);

                } catch (DateTimeParseException | NumberFormatException e) {
                    System.err.println("Error parseando línea: " + linea + " -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gastosImportados;
    }
}