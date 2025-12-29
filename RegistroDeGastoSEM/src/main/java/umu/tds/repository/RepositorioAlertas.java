package umu.tds.repository;


import java.util.List;


import umu.tds.modeloNegocio.Alerta;

public interface RepositorioAlertas{

    public List<Alerta> getAlertas(); 

    public List<Alerta> getAlertasDisparadas(); 

    public void borrarAlerta(Alerta alerta);
    

    public void agregarAlerta(Alerta alerta); 
    
}
