package umu.tds.repository.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import umu.tds.modeloNegocio.CuentaCompartida;
import umu.tds.modeloNegocio.Usuario;
import umu.tds.repository.RepositorioUsuarios;

public class RepositorioUsuariosJSON implements RepositorioUsuarios {

    private static final String FICHERO_USUARIOS = "usuarios.json";
    private static final String FICHERO_CUENTAS = "cuentas.json";
    
    private ObjectMapper mapper;

    public RepositorioUsuariosJSON() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public List<Usuario> getUsuarios() {
        File file = new File(FICHERO_USUARIOS);
        if (!file.exists()) return new ArrayList<>();
        
        try {
            return mapper.readValue(file, new TypeReference<List<Usuario>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<CuentaCompartida> getCuentas() {
        File file = new File(FICHERO_CUENTAS);
        if (!file.exists()) return new ArrayList<>();

        try {
            return mapper.readValue(file, new TypeReference<List<CuentaCompartida>>(){});
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

    @Override
    public void guardarCuentas(List<CuentaCompartida> cuentas) {
        try {
            mapper.writeValue(new File(FICHERO_CUENTAS), cuentas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
