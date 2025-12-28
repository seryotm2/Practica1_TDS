package umu.tds.modeloNegocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CuentaCompartida {
    
    private String nombre;
    private List<Usuario> participantes;
    private List<Gasto> gastos;
    
    private static int contador = 1;
    private int id;
    
    // Si el mapa está vacío, se asume reparto equitativo.
    private Map<Usuario, Double> porcentajes;

    public CuentaCompartida(String nombre, List<Usuario> participantes) {
        this.nombre = nombre;
        this.participantes = new ArrayList<>(participantes); 
        this.gastos = new ArrayList<>();
        this.porcentajes = new HashMap<>();
        this.id = this.contador;
        this.contador++;
    }
    
    public CuentaCompartida() {
        this.participantes = new ArrayList<>(); 
        this.gastos = new ArrayList<>();
        this.porcentajes = new HashMap<>();
    }
    
    public int getId() {
    	return this.id;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public void setPorcentajes(Map<Usuario, Double> porcentajes) {
        this.porcentajes = new HashMap<>(porcentajes);
    }

    public boolean addGasto(Gasto g) {
        return gastos.add(g);
    }
    
    public static void setContador(int ultimoId) {
    	contador = ultimoId + 1;
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
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CuentaCompartida other = (CuentaCompartida) obj;
        return id == other.id;
    }
    
    
    
    
}