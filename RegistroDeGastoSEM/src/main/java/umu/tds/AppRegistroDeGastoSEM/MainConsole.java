package umu.tds.AppRegistroDeGastoSEM;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.CuentaCompartida;
import umu.tds.modeloNegocio.LibroDeCuenta;
import umu.tds.modeloNegocio.Usuario;

public class MainConsole {

    private static AppControlGastos controller = AppControlGastos.getInstancia();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("  GESTIÓN DE GASTOS - MODO CONSOLA");
        System.out.println("=========================================");
        System.out.println("Bienvenido, " + controller.getUsuarioActual().getNombre());
        System.out.println("Escribe 'help' para ver la lista de comandos.");

        boolean salir = false;
        while (!salir) {
            System.out.print("\n> ");
            String linea = scanner.nextLine();
            if (linea.trim().isEmpty()) continue;

            String[] tokens = linea.split("\\s+"); //como en el trabajo de redes
            String comando = tokens[0].toLowerCase();

            try {
                switch (comando) {
                    case "help":
                        mostrarAyuda();
                        break;

                    case "add-usuario":
                        // Param: nombre, email
                        if (tokens.length < 3) throw new IllegalArgumentException("Faltan parámetros.");
                        registrarUsuario(tokens[1], tokens[2]);
                        break;

                    case "add-gasto":
                        // Param: cantidad, categoria, concepto (resto de linea)
                        if (tokens.length < 4) throw new IllegalArgumentException("Faltan parámetros.");
                        registrarGastoPersonal(tokens);
                        break;

                    case "crear-grupo":
                        // Param: nombreGrupo, amigo1, amigo2...
                        if (tokens.length < 3) throw new IllegalArgumentException("Faltan parámetros. Mínimo 1 amigo.");
                        crearGrupo(tokens);
                        break;

                    case "add-gasto-grupo":
                        // Param: nombreGrupo, pagador, cantidad, concepto...
                        if (tokens.length < 5) throw new IllegalArgumentException("Faltan parámetros.");
                        registrarGastoGrupo(tokens);
                        break;

                    case "status":
                        mostrarEstado();
                        break;

                    case "exit":
                        salir = true;
                        System.out.println("Cerrando aplicación...");
                        break;
                        
                    case "add-categoria":
                        // Param: nombre
                       if (tokens.length < 2) throw new IllegalArgumentException("Faltan parámetros.");
                       crearCategoriaManual(tokens[1]);
                       break;

                    case "importar":
                        if (tokens.length < 2) throw new IllegalArgumentException("Indica la ruta del fichero.");
                        boolean exito = controller.importarGastos(tokens[1]);
                        if (exito) System.out.println("Importación finalizada con éxito.");
                        else System.out.println("No se pudieron importar gastos o archivo vacío.");
                        break;

                    default:
                        System.out.println("Comando no reconocido. Escribe 'help'.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void mostrarAyuda() {
        System.out.println("\n--- LISTA DE COMANDOS ---");
        System.out.println("1. add-usuario <nombre> <email>");
        System.out.println("   -> Registra un amigo para compartir gastos.");
        
        System.out.println("2. add-categoria <nombre>");
        System.out.println("   -> Crea una nueva categoría de gastos (ej: 'Ocio').");
        
        System.out.println("3. add-gasto <cantidad> <categoria> <concepto>");
        System.out.println("   -> Añade un gasto personal (Fecha actual).");
        
        System.out.println("4. crear-grupo <nombre_grupo> <amigo1> <amigo2> ...");
        System.out.println("   -> Crea una cuenta compartida contigo y los amigos indicados.");
        
        System.out.println("5. add-gasto-grupo <nombre_grupo> <pagador> <cantidad> <concepto>");
        System.out.println("   -> Añade un gasto a un grupo. 'pagador' debe ser el nombre del usuario.");
        
        System.out.println("6. importar <ruta_fichero>");
        System.out.println("   -> Importa gastos desde un fichero CSV externo (ej: 'gastos.csv').");
        
        System.out.println("7. status");
        System.out.println("   -> Muestra tu Gasto Pagado, Saldos y tu Consumo Real.");
        
        System.out.println("8. exit");
        System.out.println("   -> Guarda los datos y cierra la aplicación.");
    }

    private static void registrarUsuario(String nombre, String email) {
        boolean ok = controller.registrarUsuario(nombre, email);
        if (ok) System.out.println("Usuario '" + nombre + "' registrado.");
        else System.out.println("El usuario ya existía.");
    }

    private static void registrarGastoPersonal(String[] tokens) {
        double cantidad = Double.parseDouble(tokens[1]);
        String categoria = tokens[2];
        String concepto = Arrays.stream(tokens).skip(3).collect(Collectors.joining(" "));

        boolean ok = controller.registrarGasto(concepto, categoria, cantidad, LocalDate.now());
        if (ok) System.out.println("Gasto personal registrado: " + cantidad + "€ en " + categoria);
        else System.out.println("Error al registrar gasto.");
    }

    private static void crearGrupo(String[] tokens) {
        String nombreGrupo = tokens[1];
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 2; i < tokens.length; i++) {
            Optional<Usuario> u = controller.obtenerUsuario(tokens[i]);
            if (u.isPresent()) {
                usuarios.add(u.get());
            } else {
                System.out.println("Advertencia: Usuario '" + tokens[i] + "' no encontrado. Se ignora.");
            }
        }

        if (usuarios.isEmpty()) {
            System.out.println("Error: No se encontraron usuarios válidos para el grupo.");
            return;
        }

        controller.crearCuentaCompartida(nombreGrupo, usuarios);
        System.out.println("Grupo '" + nombreGrupo + "' creado con éxito (Tú incluido automáticamente).");
    }

    private static void registrarGastoGrupo(String[] tokens) {
        String nombreGrupo = tokens[1];
        String nombrePagador = tokens[2];
        double cantidad = Double.parseDouble(tokens[3]);
        String concepto = Arrays.stream(tokens).skip(4).collect(Collectors.joining(" "));

        Optional<CuentaCompartida> grupoOpt = controller.buscarCuenta(nombreGrupo);
        if (grupoOpt.isEmpty()) {
            System.out.println("Error: Grupo '" + nombreGrupo + "' no encontrado.");
            return;
        }

        Usuario pagador = null;
        if (nombrePagador.equalsIgnoreCase(controller.getUsuarioActual().getNombre())) {
            pagador = controller.getUsuarioActual();
        } else {
            Optional<Usuario> u = controller.obtenerUsuario(nombrePagador);
            if (u.isPresent()) pagador = u.get();
        }

        if (pagador == null) {
            System.out.println("Error: Usuario pagador '" + nombrePagador + "' no encontrado.");
            return;
        }

        boolean ok = controller.registrarGastoCompartido(grupoOpt.get(), concepto, cantidad, LocalDate.now(), pagador);
        if (ok) System.out.println("Gasto compartido registrado correctamente.");
        else System.out.println("Error: El usuario no pertenece al grupo o fallo interno.");
    }

    private static void mostrarEstado() {
        System.out.println("\n--- ESTADO FINANCIERO ---");
        double gastoPagado = LibroDeCuenta.getInstancia().getGastoGlobal();
        double saldoTotalGrupos = 0;
        
        List<CuentaCompartida> grupos = controller.getCuentasCompartidas();
        
        if (grupos.isEmpty()) {
            System.out.println(">> No tienes grupos compartidos.");
        } else {
            System.out.println(">> Grupos Compartidos:");
            for (CuentaCompartida cc : grupos) {
                Usuario yo = controller.getUsuarioActual();
                double saldo = cc.getSaldo(yo);
                saldoTotalGrupos += saldo; // Acumulamos el saldo (+ o -)
                
                String estado = saldo >= 0 ? "Te deben" : "Debes";
                System.out.println("   [" + cc.getNombre() + "] -> Saldo: " + String.format("%.2f", saldo) + "€ (" + estado + ")");
            }
        }
        double consumoReal = gastoPagado - saldoTotalGrupos;

        System.out.println("--------------------------------");
        System.out.println(">> Pagos realizados (Tu bolsillo): " + String.format("%.2f", gastoPagado) + "€");
        
        if (saldoTotalGrupos < 0) {
            System.out.println(">> (+) Deudas acumuladas (Has consumido pero no pagado): " + String.format("%.2f", Math.abs(saldoTotalGrupos)) + "€");
        } else {
            System.out.println(">> (-) Dinero que te deben (Has pagado por otros): " + String.format("%.2f", saldoTotalGrupos) + "€");
        }
        
        System.out.println("================================");
        System.out.println(">> TU GASTO REAL (Consumo): " + String.format("%.2f", consumoReal) + "€");
        System.out.println("================================");
    }
    
    private static void crearCategoriaManual(String nombre) {
        boolean ok = controller.registrarCategoria(nombre);
        if (ok) {
            System.out.println("Categoría '" + nombre + "' creada exitosamente.");
        } else {
            System.out.println("La categoría '" + nombre + "' ya existía.");
        }
    }
}