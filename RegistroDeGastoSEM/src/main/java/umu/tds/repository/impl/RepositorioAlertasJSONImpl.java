package umu.tds.repository.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import umu.tds.modeloNegocio.Alerta;
import umu.tds.modeloNegocio.Gasto;
import umu.tds.repository.RepositorioAlertas;





public class RepositorioAlertasJSONImpl implements RepositorioAlertas{

	private static final String REPOSITORY_PATH = "data/alertas";
	private static final String FICHERO_ALERTAS = REPOSITORY_PATH + "/alertas.json";
	private static final String FICHERO_NOTIFICACIONES = REPOSITORY_PATH + "/notificaciones.json";
    private ObjectMapper mapper;
	private List<Alerta> alertas;
	private List<Alerta> notifs;

	public RepositorioAlertasJSONImpl() {
        this.alertas = new ArrayList<>();
        this.notifs = new ArrayList<>();
        
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        try {
            Path pathDir = Paths.get(REPOSITORY_PATH);
            if (!Files.exists(pathDir)) {
                Files.createDirectories(pathDir);
            }
            
            Path pathAlertas = Paths.get(FICHERO_ALERTAS);
            if (!Files.exists(pathAlertas)) {
                Files.createFile(pathAlertas);
            }

            Path pathNotifs = Paths.get(FICHERO_NOTIFICACIONES);
            if (!Files.exists(pathNotifs)) {
                Files.createFile(pathNotifs);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        getAlertasH();
        getNotifsH();
    }

    public List<Alerta> getAlertas() {
    	File file = new File(FICHERO_ALERTAS);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        
        try {
            return mapper.readValue(file, new TypeReference<List<Alerta>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Alerta> getAlertasDisparadas() {
        return getAlertas().stream()
                .filter(Alerta::estaDisparada)
                .collect(Collectors.toList());
    }
    
    public List<Alerta> getNotificaciones() {
    	File file = new File(FICHERO_NOTIFICACIONES);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        
        try {
            return mapper.readValue(file, new TypeReference<List<Alerta>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    @Override
    public void agregarAlerta(Alerta alerta) {
        alertas.add(alerta);
        guardarLista(FICHERO_ALERTAS, alertas);
    }
    
    @Override
    public void agregarNotificacion(Alerta alerta) {
        if(!notifs.contains(alerta)) {
            notifs.add(alerta);
            guardarLista(FICHERO_NOTIFICACIONES, notifs);
        }
    }
    
    @Override
    public void borrarAlerta(Alerta alerta) {
        if(alertas.remove(alerta)) {
            guardarLista(FICHERO_ALERTAS, alertas);
        }
    }
    
    @Override
    public void borrarNotificacion(Alerta alerta) {
        if(notifs.remove(alerta)) {
            guardarLista(FICHERO_NOTIFICACIONES, notifs);
        }
    }
    
    public List<Alerta> getAlertasH() {
    	if(!alertas.isEmpty())
			return alertas;
		Path ficheroHis = Paths.get(FICHERO_ALERTAS);
		exceptionIfNofile(ficheroHis);
		try {
			if (Files.size(ficheroHis) == 0)
			    return new ArrayList<Alerta>();
			alertas.addAll(mapper.readValue(ficheroHis.toFile(), new TypeReference<ArrayList<Alerta>>() {}));
		} catch (Exception e) {e.printStackTrace();}
		return alertas;
    }
    
    public List<Alerta> getNotifsH() {
    	if(!notifs.isEmpty())
			return notifs;
		Path ficheroHis = Paths.get(FICHERO_NOTIFICACIONES);
		exceptionIfNofile(ficheroHis);
		try {
			if (Files.size(ficheroHis) == 0)
			    return new ArrayList<Alerta>();
			alertas.addAll(mapper.readValue(ficheroHis.toFile(), new TypeReference<ArrayList<Alerta>>() {}));
		} catch (Exception e) {e.printStackTrace();}
		return notifs;
    }

    private void exceptionIfNofile(Path pathFile) {
		if(!Files.exists(pathFile)) {
			throw new IllegalStateException("No existe " + pathFile + "en memoria");
		}
	}
    
    private void guardarLista(String path, List<Alerta> lista) {
        try {
            mapper.writerFor(new TypeReference<List<Alerta>>() {}).writeValue(new java.io.File(path), lista);
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }
}
