package umu.tds.repository.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.Gasto;
import umu.tds.repository.RepositorioGastos;

public class RepositorioGastosJSONImpl implements RepositorioGastos{
	
	private static final String REPOSITORY_PATH = "data/gastos" ;
	private static final String HISTORIAL_PATH = REPOSITORY_PATH +"/"+ "historial.json" ;
	private static final String CATEGORIES_PATH = REPOSITORY_PATH + "/categorias";
	private static final String CATEGORY_LIST_PATH = REPOSITORY_PATH + "/categoryList.json";
	private Set<Gasto> historial;
	ObjectMapper mapper;
		
	
	public RepositorioGastosJSONImpl() {
		historial = new TreeSet<Gasto>();
		if(!Files.exists(Paths.get(REPOSITORY_PATH)))
			try {
				// Se crea el directorio donde se van a guardar los gastos
				Files.createDirectories(Paths.get(REPOSITORY_PATH));
				Files.createDirectories(Paths.get(HISTORIAL_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			}
		//Cofiguracion del mapper.
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	@Override
	public Set<Gasto> getGastos(Categoria categoria) {
		TreeSet<Gasto> gastos = new TreeSet<>();
		Path ficheroCat = Paths.get(CATEGORIES_PATH + "/"+ categoria.getNombreCategoria() + ".json");
		
		exceptionIfNofile(ficheroCat);
		try {
			if (Files.size(ficheroCat) == 0)
			    return gastos;
			gastos = mapper.readValue(ficheroCat.toFile(), new TypeReference<TreeSet<Gasto>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gastos;
	}

	@Override
	public void updateGastos(Categoria categoria) {
		Path ficheroCat = Paths.get(CATEGORIES_PATH + "/"+ categoria.getNombreCategoria() + ".json");
		
		exceptionIfNofile(ficheroCat);
		Set<Gasto> gastos = categoria.getGastos();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(ficheroCat.toFile(), gastos);
		} catch (Exception e) {e.printStackTrace();} 		
	}

	@Override
	public void eliminarGasto(Gasto gasto) {
		if(historial.remove(gasto)) {
			Path ficheroCat = Paths.get(HISTORIAL_PATH);
			try {
				mapper.writerWithDefaultPrettyPrinter().writeValue(ficheroCat.toFile(), historial);
			} catch (Exception e) {e.printStackTrace();} 
		}
	}

	@Override
	public Set<Gasto> getHistorico() {
		if(!historial.isEmpty())
			return historial;
		Path ficheroHis = Paths.get(HISTORIAL_PATH);
		exceptionIfNofile(ficheroHis);
		try {
			if (Files.size(ficheroHis) == 0)
			    return new TreeSet<>();
			historial = mapper.readValue(ficheroHis.toFile(), new TypeReference<TreeSet<Gasto>>() {});
		} catch (Exception e) {e.printStackTrace();}
		return historial;
	}

	@Override
	public Set<String> getIdCategorias() {
		Path fichero = Paths.get(CATEGORY_LIST_PATH );

		if(!Files.exists(fichero))
			return new TreeSet<>();	
		TreeSet<String> categorias = new TreeSet<>();
		try {
			if (Files.size(fichero) == 0)
			    return categorias;
			categorias = mapper.readValue(fichero.toFile(), new TypeReference<TreeSet<String>>() {});
		} catch (Exception e) {e.printStackTrace();}
		return categorias;
	}

	@Override
	public void registrarCategoria(String nombreCategoria) {
		Path catList = Paths.get(CATEGORY_LIST_PATH );
		Set<String> cat = new TreeSet<>();
		if(!Files.exists(catList)) {
			try {
				Files.createDirectories(catList);
			} catch (IOException e) {e.printStackTrace();}
			cat.add(nombreCategoria);
			try {
				mapper.writerWithDefaultPrettyPrinter().writeValue(catList.toFile(), cat);
			} catch (Exception e) {e.printStackTrace();}
		}
		cat = getIdCategorias();
		if(cat.add(nombreCategoria)) {
			try {
				mapper.writerWithDefaultPrettyPrinter().writeValue(catList.toFile(), cat);
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
	private void exceptionIfNofile(Path pathFile) {
		if(!Files.exists(pathFile)) {
			throw new IllegalStateException("No existe " + pathFile
					+ "en memoria");
		}
	}

}
