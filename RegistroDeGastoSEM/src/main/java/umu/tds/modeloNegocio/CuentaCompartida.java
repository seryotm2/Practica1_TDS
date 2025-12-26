package umu.tds.modeloNegocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuentaCompartida {
    
    private String nombre;
    private List<Usuario> participantes;
    private List<Gasto> gastos;
    
    // Si el mapa está vacío, se asume reparto equitativo.
    private Map<Usuario, Double> porcentajes;

    public CuentaCompartida(String nombre, List<Usuario> participantes) {
        this.nombre = nombre;
        this.participantes = new ArrayList<>(participantes); 
        this.gastos = new ArrayList<>();
        this.porcentajes = new HashMap<>();
    }
    
    
    public void setPorcentajes(Map<Usuario, Double> porcentajes) {
        this.porcentajes = new HashMap<>(porcentajes);
    }

    public boolean addGasto(Gasto g) {
        return gastos.add(g);
    }

    /**
     * Calcula el saldo de un usuario en esta cuenta.
     * Saldo = Lo que ha pagado - Lo que debería haber pagado.
     * Positivo: Le deben dinero. Negativo: Debe dinero.
     */
    public double getSaldo(Usuario u) {
        double totalGastado = gastos.stream().mapToDouble(Gasto::getCantidad).sum();
        double pagadoPorUsuario = gastos.stream()
                .filter(g -> g.getUsuario().equals(u))
                .mapToDouble(Gasto::getCantidad).sum();

        double cuotaUsuario = 0.0;

        if (porcentajes.isEmpty()) {
            // Reparto equitativo
            if (!participantes.isEmpty()) {
                cuotaUsuario = totalGastado / participantes.size();
            }
        } else {
            // Reparto por porcentaje
            double porcentaje = porcentajes.getOrDefault(u, 0.0);
            cuotaUsuario = totalGastado * (porcentaje / 100.0);
        }

        return pagadoPorUsuario - cuotaUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Usuario> getParticipantes() {
        return Collections.unmodifiableList(participantes);
    }

    public List<Gasto> getGastos() {
        return Collections.unmodifiableList(gastos);
    }
}