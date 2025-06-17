package unl.dance.base.controller.dao.dao_models;

import java.util.HashMap;

import unl.dance.base.controller.Utiles;
import unl.dance.base.controller.dao.AdapterDao;
import unl.dance.base.controller.data_struct.list.LinkedList;
import unl.dance.base.models.Cancion;

public class DaoCancion extends AdapterDao<Cancion> {

    private Cancion obj;

    public DaoCancion() {
        super(Cancion.class);
        // TODO Auto-generated constructor stub
    }

    public Cancion getObj() {
        if (obj == null) {
            this.obj = new Cancion();
        }
        return this.obj;
    }

    public void setObj(Cancion obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    // Método principal que ordena un arreglo de HashMaps usando QuickSort
public void quickSort(HashMap arr[], int begin, int end, Integer type, String attribute) {
    if (begin < end) {
        int partitionIndex = partition(arr, begin, end, type, attribute); // separa los menores y mayores
        quickSort(arr, begin, partitionIndex - 1, type, attribute);       // ordena la parte izquierda
        quickSort(arr, partitionIndex + 1, end, type, attribute);         // ordena la parte derecha
    }
}

// Divide el arreglo en dos partes según el valor del atributo
private int partition(HashMap<String, Object>[] arr, int begin, int end, Integer type, String attribute) {
    HashMap<String, Object> pivot = arr[end];  // Usamos el último elemento como pivote
    int i = begin - 1;

    for (int j = begin; j < end; j++) {
        // Comparación para orden ascendente o descendente
        boolean condicion = (type == Utiles.ASCENDENTE)
            ? arr[j].get(attribute).toString().compareTo(pivot.get(attribute).toString()) < 0
            : arr[j].get(attribute).toString().compareTo(pivot.get(attribute).toString()) > 0;

        if (condicion) {
            i++;
            HashMap<String, Object> temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    // Coloca el pivote en su lugar final
    HashMap<String, Object> temp = arr[i + 1];
    arr[i + 1] = arr[end];
    arr[end] = temp;

    return i + 1;
}

    // Convierte todas las canciones en HashMap con datos útiles
public LinkedList<HashMap<String, Object>> all() throws Exception {
    LinkedList<HashMap<String, Object>> lista = new LinkedList<>();
    if (!this.listAll().isEmpty()) {
        Cancion[] arreglo = this.listAll().toArray();
        for (int i = 0; i < arreglo.length; i++) {
            lista.add(toDict(arreglo[i], i));
        }
    }
    return lista;
}

// Convierte una canción en un HashMap con nombre, género, álbum, duración, etc.
private HashMap<String, Object> toDict(Cancion arreglo, Integer i) throws Exception {
    HashMap<String, Object> aux = new HashMap<>();
    DaoAlbum db = new DaoAlbum();
    DaoGenero dc = new DaoGenero();

    aux.put("id", arreglo.getId().toString(i));
    aux.put("nombre", arreglo.getNombre());
    aux.put("genero", dc.get(arreglo.getId_genero()).getNombre());
    aux.put("album", db.get(arreglo.getId_album()).getNombre());
    aux.put("duracion", arreglo.getDuracion());
    aux.put("url", arreglo.getUrl());
    aux.put("tipo", arreglo.getTipo().toString());
    return aux;
}

    // Ordena las canciones por un atributo (por ejemplo, nombre o duración)
public LinkedList<HashMap<String, Object>> orderByCancion(Integer type, String attribute) throws Exception {
    LinkedList<HashMap<String, Object>> lista = all();
    if (!listAll().isEmpty()) {
        HashMap[] arr = lista.toArray();
        quickSort(arr, 0, arr.length - 1, type, attribute);  // Usa QuickSort
        lista.toList(arr);  // Vuelve a LinkedList
    }
    return lista;
}

    // Método para estimar en qué mitad del arreglo buscar según la primera letra
private Integer bynaryLineal(HashMap<String, Object>[] arr, String attribute, String text) {
    Integer half = 0;
    if (!(arr.length == 0) && !text.isEmpty()) {
        half = arr.length / 2;
        int aux = 0;

        // Compara la primera letra del texto con la del centro del arreglo
        if (text.trim().toLowerCase().charAt(0) > arr[half].get(attribute).toString().trim().toLowerCase().charAt(0)) {
            aux = 1;
        } else if (text.trim().toLowerCase().charAt(0) < arr[half].get(attribute).toString().trim().toLowerCase().charAt(0)) {
            aux = -1;
        }

        half = half * aux;
    }
    return half;
}

    public LinkedList<HashMap<String, Object>> search(String attribute, String text, Integer type) throws Exception {
    // Obtener toda la lista de canciones como HashMaps
    LinkedList<HashMap<String, Object>> lista = all();
    // Crear una nueva lista para guardar los resultados que coincidan con la búsqueda
    LinkedList<HashMap<String, Object>> resp = new LinkedList<>();

    // Verificar que la lista no esté vacía para proceder con la búsqueda
    if (!lista.isEmpty()) {
        // Ordenar la lista por el atributo especificado en orden ascendente
        lista = orderByCancion(Utiles.ASCENDENTE, attribute);

        // Convertir la lista ordenada a un arreglo para poder hacer búsquedas más rápidas
        HashMap<String, Object>[] arr = lista.toArray();

        // Usar un método que busca una posición aproximada (basado en comparación lineal/binary)
        Integer n = bynaryLineal(arr, attribute, text);

        // Evaluar el tipo de búsqueda que se realizará según el valor del parámetro 'type'
        switch (type) {
            case 1:
                // Tipo 1: Búsqueda con rango usando el índice estimado 'n'
                if (n > 0) {
                    // Si la posición estimada es mayor que 0, buscar desde 'n' hasta el final del arreglo
                    for (int i = n; i < arr.length; i++) {
                        // Verificar si el valor del atributo empieza con el texto buscado (ignorando mayúsculas/minúsculas)
                        if (arr[i].get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            // Si coincide, agregar el elemento a la lista de resultados
                            resp.add(arr[i]);
                        }
                    }
                } else if (n < 0) {
                    // Si la posición estimada es menor que 0, se invierte el rango para buscar desde el inicio hasta la posición 'n' absoluta
                    n *= -1;
                    for (int i = 0; i < n; i++) {
                        if (arr[i].get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(arr[i]);
                        }
                    }
                } else {
                    // Si no hay posición estimada (n == 0), se busca en todo el arreglo
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(arr[i]);
                        }
                    }
                }
                break;

            case 2:
                // Tipo 2: Comportamiento igual al tipo 1, busca en un rango según 'n'
                if (n > 0) {
                    for (int i = n; i < arr.length; i++) {
                        if (arr[i].get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(arr[i]);
                        }
                    }
                } else if (n < 0) {
                    n *= -1;
                    for (int i = 0; i < n; i++) {
                        if (arr[i].get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(arr[i]);
                        }
                    }
                } else {
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(arr[i]);
                        }
                    }
                }
                break;

            default:
                // En caso de que 'type' no sea 1 ni 2, se busca en toda la lista sin rangos
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                        resp.add(arr[i]);
                    }
                }
                break;
        }
    }

    // Devolver la lista con todos los elementos que cumplen el criterio de búsqueda
    return resp;
}

}
