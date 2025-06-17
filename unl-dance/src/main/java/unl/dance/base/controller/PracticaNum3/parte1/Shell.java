package unl.dance.base.controller.PracticaNum3.parte1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import unl.dance.base.controller.data_struct.list.LinkedList;

public class Shell {

    private BufferedReader FileReadM(String fileName) throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new Exception("Archivo '" + fileName + "' no encontrado en recursos");
        }
        return new BufferedReader(new InputStreamReader(input));
    }


    // Este método ordena una lista usando el algoritmo Shell Sort
public static void shell(LinkedList<Integer> list){
    int inta, i, aux;
    boolean band;

    // inta es la distancia entre elementos que vamos a comparar
    inta = list.getLength();  // empezamos con el tamaño completo de la lista

    // Seguimos mientras la distancia (inta) sea mayor que 0
    while(inta > 0){
        inta = inta / 2;  // reducimos la distancia a la mitad

        band = true;  // usamos esta bandera para saber si hubo cambios

        // Seguimos comparando mientras haya cambios (intercambios)
        while(band){
            band = false;  // al inicio de cada ciclo no hemos hecho cambios todavía
            i = 0;

            // Recorremos la lista comparando elementos separados por la distancia "inta"
            while ((i + inta) <= list.getLength() - 1){
                // Si el número actual es mayor que el número que está "inta" posiciones adelante
                if (list.get(i) > list.get(i + inta)){
                    // Intercambiamos los dos valores
                    aux = list.get(i);
                    list.update(list.get(i + inta), i);
                    list.update(aux, i + inta);

                    band = true;  // como hicimos un cambio, volvemos a revisar
                }
                i = i + 1;  // pasamos al siguiente par de elementos
            }
        }
    }
}

    public void DataProcess() {
        LinkedList<Integer> list = new LinkedList<>();
        int i = 0;

        try (BufferedReader br = FileReadM("data.txt")) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    list.add(Integer.parseInt(linea.trim()));
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }


        long startTime = System.nanoTime();

        shell(list);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println("\nLista ordenada:");
        printList(list);

        System.out.println("\nTiempo de ejecución del Shell: " + duration + " ns");
    }

    public void printList(LinkedList<Integer> list) {
        for (int i = 0; i < list.getLength(); i++) {
            System.out.print(list.get(i));
            if (i < list.getLength() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Shell app = new Shell();
        app.DataProcess();
    }
}
