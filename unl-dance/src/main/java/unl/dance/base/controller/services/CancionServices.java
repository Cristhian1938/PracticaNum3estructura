package unl.dance.base.controller.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.NotEmpty;
import unl.dance.base.controller.dao.dao_models.DaoAlbum;
import unl.dance.base.controller.dao.dao_models.DaoCancion;
import unl.dance.base.controller.dao.dao_models.DaoGenero;
import unl.dance.base.controller.data_struct.list.LinkedList;
import unl.dance.base.models.Album;
import unl.dance.base.models.Genero;
import unl.dance.base.models.TipoArchivoEnum;

@BrowserCallable
@Transactional(propagation = Propagation.REQUIRES_NEW)
@AnonymousAllowed

public class CancionServices {

    private DaoCancion db;

    public CancionServices() {
        db = new DaoCancion();
    }

    public void create(@NotEmpty String nombre, Integer id_genero, Integer duracion, @NotEmpty String url, @NotEmpty String tipo, Integer id_album) throws Exception {
        if (nombre.trim().length() > 0 && url.trim().length() > 0 && tipo.trim().length() > 0 && duracion > 0 && id_genero > 0 && id_album > 0) {
            db.getObj().setNombre(nombre);
        }
        db.getObj().setDuracion(duracion);
        db.getObj().setId_album(id_album);
        db.getObj().setTipo(TipoArchivoEnum.valueOf(tipo));
        db.getObj().setUrl(url);
        db.getObj().setId_genero(id_genero);
        if (!db.save()) {
            throw new Exception("No se pudo guardar los datos de Cancion");
        }
    }

    public void update(Integer id, @NotEmpty String nombre, Integer id_genero, Integer duracion, @NotEmpty String url, @NotEmpty String tipo, Integer id_album) throws Exception {
        if (id != null && id > 0 && nombre.trim().length() > 0 && url.trim().length() > 0 && tipo.trim().length() > 0 && duracion > 0 && id_genero > 0 && id_album > 0) {
            db.setObj(db.listAll().get(id));
        }
        db.getObj().setNombre(nombre);
        db.getObj().setDuracion(duracion);
        db.getObj().setId_album(id_album);
        db.getObj().setTipo(TipoArchivoEnum.valueOf(tipo));
        db.getObj().setUrl(url);
        db.getObj().setId_genero(id_genero);
        if (!db.update(id)) {
            throw new Exception("No se pudo guardar los datos de Cancion");
        }
    }

    public List<HashMap> listaAlbumCombo() {
        List<HashMap> lista = new ArrayList<>();
        DaoAlbum da = new DaoAlbum();
        if (!db.listAll().isEmpty()) {
            Album[] arreglo = da.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("value", arreglo[i].getId().toString());
                aux.put("label", arreglo[i].getNombre());
                lista.add(aux);
            }
        }
        return lista;
    }

    public List<HashMap> listaGeneroCombo() {
        List<HashMap> lista = new ArrayList<>();
        DaoGenero da = new DaoGenero();
        if (!db.listAll().isEmpty()) {
            Genero[] arreglo = da.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("value", arreglo[i].getId().toString());
                aux.put("label", arreglo[i].getNombre());
                lista.add(aux);
            }
        }
        return lista;
    }

   // Devuelve una lista de strings con los nombres de todos los valores del enum TipoArchivoEnum
public List<String> listTipo() {
    List<String> lista = new ArrayList<>();
    // Recorre todos los valores del enum y los agrega a la lista como string
    for (TipoArchivoEnum r : TipoArchivoEnum.values()) {
        lista.add(r.toString());
    }
    return lista;
}

// Devuelve una lista con todos los elementos que obtiene del método all() de la base de datos (db)
public List<HashMap> listAll() throws Exception {
    // Convierte el arreglo retornado por db.all() a una lista y la devuelve
    return Arrays.asList(db.all().toArray());
}

// Devuelve una lista con los elementos ordenados según el atributo y tipo especificados
public List<HashMap> order(String attribute, Integer type) throws Exception {
    // Llama al método orderByCancion del objeto db para ordenar, convierte el resultado a lista y lo retorna
    return Arrays.asList(db.orderByCancion(type, attribute).toArray());
}

// Realiza una búsqueda usando el método search de db y devuelve una lista con los resultados encontrados
public List<HashMap> search(String attribute, String text, Integer type) throws Exception {
    LinkedList<HashMap<String, Object>> lista = db.search(attribute, text, type);
    // Si la lista no está vacía, convierte el arreglo a lista y la devuelve
    if (!lista.isEmpty()) {
        return Arrays.asList(lista.toArray());
    } else {
        // Si no hay resultados, retorna una lista vacía
        return new ArrayList<>();
    }
}

    
}
