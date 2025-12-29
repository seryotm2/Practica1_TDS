package umu.tds.repository.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    
    private ObjectMapper mapper;

    public RepositorioUsuariosJSON() {
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
    }

    @Override
    public List<Usuario> getUsuarios() {
        File file = new File(FICHERO_USUARIOS);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        
        try {
            return mapper.readValue(file, new TypeReference<List<Usuario>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void guardarUsuarios(List<Usuario> usuarios) {
        try {
            mapper.writeValue(new File(FICHERO_USUARIOS), usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}