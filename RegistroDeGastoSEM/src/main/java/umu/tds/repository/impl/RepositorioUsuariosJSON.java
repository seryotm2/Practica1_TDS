package umu.tds.repository.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import umu.tds.modeloNegocio.Usuario;
import umu.tds.repository.RepositorioUsuarios;

public class RepositorioUsuariosJSON implements RepositorioUsuarios {
	
	private static final String REPOSITORY_PATH = "data/usuarios";
	private static final String FICHERO_USUARIOS = REPOSITORY_PATH + "/usuarios.json";
    private List<Usuario> usuarios;
    private ObjectMapper mapper;

    public RepositorioUsuariosJSON() {
    	usuarios = new LinkedList<>();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Path pathDir = Paths.get(REPOSITORY_PATH);
        if (!Files.exists(pathDir)) {
            try {
                Files.createDirectories(pathDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getUsuarios();
    }

    @Override
    public List<Usuario> getUsuarios() {
    	if(!usuarios.isEmpty())
    		return usuarios;
        File file = new File(FICHERO_USUARIOS);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        
        try {
            usuarios.addAll(mapper.readValue(file, new TypeReference<List<Usuario>>(){}));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return usuarios;
    }

    @Override
    public void guardarUsuarios(Collection<Usuario> usuarios) {
        try {
            mapper.writeValue(new File(FICHERO_USUARIOS), usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}