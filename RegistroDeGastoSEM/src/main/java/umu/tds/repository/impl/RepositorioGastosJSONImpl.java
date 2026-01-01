package umu.tds.repository.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import umu.tds.modeloNegocio.Categoria;
import umu.tds.modeloNegocio.CuentaCompartida;
import umu.tds.modeloNegocio.Directorio;
import umu.tds.modeloNegocio.Gasto;
import umu.tds.repository.RepositorioGastos;

public class RepositorioGastosJSONImpl implements RepositorioGastos{
	
	private static final String REPOSITORY_PATH = "data/gastos" ;
	private static final String HISTORIAL_PATH = REPOSITORY_PATH +"/historial.json" ;
	private static final String CATEGORIES_PATH = REPOSITORY_PATH + "/categorias";
	private static final String CATEGORY_LIST_PATH = REPOSITORY_PATH + "/categoryList.json";
	private static final String CUENTAS_PATH = REPOSITORY_PATH + "/cuentas/cuentasCompartidas.json" ;
	private Set<Gasto> historial;
	ObjectMapper mapper;
		
	
	public RepositorioGastosJSONImpl() {
        
		historial = new TreeSet<>(Comparator.reverseOrder());
		if(!Files.exists(Paths.get(REPOSITORY_PATH)))
			try {
				// Se crea el directorio donde se van a guardar los gastos
				Files.createDirectories(Paths.get(REPOSITORY_PATH));
				Files.createDirectories(Paths.get(CATEGORIES_PATH));
				Files.createFile(Paths.get(HISTORIAL_PATH));
				Files.createDirectories(Paths.get(CUENTAS_PATH).getParent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		//Cofiguracion del mapper.
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		getHistorico();
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
		gastos.addAll(resolverUsuarios(gastos));
		return gastos;
	}

	@Override
	public void updateGastos(Categoria categoria) {
		Path ficheroCat = Paths.get(CATEGORIES_PATH + "/"+ categoria.getNombreCategoria() + ".json");
		
		exceptionIfNofile(ficheroCat);
		Set<Gasto> gastos = categoria.getGastos();
		try {
			mapper.writerFor(new TypeReference<Set<Gasto>>() {}).withDefaultPrettyPrinter().writeValue(ficheroCat.toFile(), gastos);
		} catch (Exception e) {e.printStackTrace();} 		
	}

	@Override
	public void eliminarGasto(Gasto gasto) {
		if(historial.remove(gasto)) {
			Path ficheroHis = Paths.get(HISTORIAL_PATH);
			try {
				mapper.writerFor(new TypeReference<Set<Gasto>>() {}).withDefaultPrettyPrinter().writeValue(ficheroHis.toFile(), historial);
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
			historial.addAll(mapper.readValue(ficheroHis.toFile(), new TypeReference<TreeSet<Gasto>>() {}));
		} catch (Exception e) {e.printStackTrace();}
		historial.addAll(resolverUsuarios(historial));
		return historial;
	}
	
	public void updateHistorico(Gasto newGasto) {
		if(this.historial.isEmpty())
			getHistorico();
		historial.add(newGasto);
		Path ficheroHis = Paths.get(HISTORIAL_PATH);
		try {
			mapper.writerFor(new TypeReference<Set<Gasto>>() {}).withDefaultPrettyPrinter().writeValue(ficheroHis.toFile(), historial);
		} catch (Exception e) {e.printStackTrace();}	
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
			try {	//Si no existe, se crea el fichero
				Files.createFile(catList);
			} catch (IOException e) {e.printStackTrace();}
			cat.add(nombreCategoria);
			try {
				mapper.writerWithDefaultPrettyPrinter().writeValue(catList.toFile(), cat);
			} catch (Exception e) {e.printStackTrace();}
		}
		else {	// Si existe, se actualiza la lista de categorías
			cat = getIdCategorias();
			if(cat.add(nombreCategoria)) {
				try {	
					mapper.writerWithDefaultPrettyPrinter().writeValue(catList.toFile(), cat);
				} catch (Exception e) {e.printStackTrace();}
			}
		}
		Path catFile = Paths.get(CATEGORIES_PATH + "/"+ nombreCategoria + ".json");
		if(!Files.exists(catFile)) {
			try {//Creación del fichero propio de la categoría
				Files.createFile(Paths.get(CATEGORIES_PATH + "/"+ nombreCategoria + ".json"));
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	private void exceptionIfNofile(Path pathFile) {
		if(!Files.exists(pathFile)) {
			throw new IllegalStateException("No existe " + pathFile
					+ "en memoria");
		}
	}

	@Override
	public List<CuentaCompartida> getCuentas() {
		Path fCuentas = Paths.get(CUENTAS_PATH);
		if(!Files.exists(fCuentas))			
			return new LinkedList<CuentaCompartida>();
	
		List<CuentaCompartida> lista = new LinkedList<>();
		try {
			if (Files.size(fCuentas) == 0)
			    return lista;
			
			lista = mapper.readValue(fCuentas.toFile(), new TypeReference<LinkedList<CuentaCompartida>>() {});
		} catch (Exception e) {e.printStackTrace();}		
		return lista;
	}
	
	public void updateCuentas(List<CuentaCompartida> lista) {
		Path fCuentas = Paths.get(CUENTAS_PATH);
		if(!Files.exists(fCuentas)) {
			try {//Creación del fichero de cuentas
				Files.createFile(fCuentas);
			} catch (IOException e) {e.printStackTrace();}
		}
		try {	
			mapper.writerWithDefaultPrettyPrinter().writeValue(fCuentas.toFile(), lista);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	@Override
    public void addGasto(Gasto gasto) {
        historial.add(gasto);
        Path ficheroHis = Paths.get(HISTORIAL_PATH);
        try {
            mapper.writerFor(new TypeReference<Set<Gasto>>() {})
                  .withDefaultPrettyPrinter()
                  .writeValue(ficheroHis.toFile(), historial);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private Set<Gasto> resolverUsuarios(Collection<Gasto> gastos) {
		if(gastos.isEmpty())
			return new TreeSet<>();
		 Directorio dir = Directorio.getInstancia();
		 Set<Gasto> res = new TreeSet<>();
		for(Gasto gAct: gastos) {
			if (gAct.getUsuarioId() != null && gAct.getUsuario() == null) {
                dir.buscarUsuario(gAct.getUsuarioId())
                   .ifPresent(gAct::setUsuario);
                res.add(gAct);
            }
		}
		return res;
	}

}
