package unl.dance.base.controller.PracticaNum3.parte1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import unl.dance.base.controller.data_struct.list.LinkedList;

public class QuickSort {

    private BufferedReader FileReadM(String fileName) throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new Exception("Archivo '" + fileName + "' no encontrado en recursos");
        }
        return new BufferedReader(new InputStreamReader(input));
    }


    // Este método ordena la lista usando el algoritmo QuickSort
public void quickSort(LinkedList<Integer> list, int low, int high) {
    // Solo seguimos si hay más de un elemento para ordenar
    if (low < high) {
        // Usamos un número como punto de referencia (pivote)
        // y colocamos ese número en el lugar donde debe ir
        int pi = partition(list, low, high);

        // Ordenamos los números que están antes del pivote
        quickSort(list, low, pi - 1);

        // Ordenamos los números que están después del pivote
        quickSort(list, pi + 1, high);
    }
}


    // Este método organiza los números alrededor de un "pivote"
private int partition(LinkedList<Integer> list, int low, int high) {
    // Elegimos el último número como el pivote (nuestro número guía)
    int pivot = list.get(high);

    // i nos ayuda a saber dónde poner los números más pequeños que el pivote
    int i = low - 1;

    // Recorremos todos los números desde 'low' hasta uno antes del pivote
    for (int j = low; j < high; j++) {
        // Si el número actual es menor o igual al pivote
        if (list.get(j) <= pivot) {
            i++; // avanzamos el espacio para los números pequeños

            // Intercambiamos los números: el pequeño va al inicio, y el otro se mueve
            int temp = list.get(i);
            list.update(list.get(j), i);
            list.update(temp, j);
        }
    }

    // Al final, ponemos el pivote en su lugar correcto
    int temp = list.get(i + 1);
    list.update(list.get(high), i + 1);
    list.update(temp, high);

    // Devolvemos la nueva posición del pivote
    return i + 1;
}

    public void PrintResult(LinkedList<Integer> list, String titulo) {
        System.out.println(titulo + ":");
        for (int i = 0; i < list.getLength(); i++) {
            System.out.print(list.get(i));
            if (i < list.getLength() - 1) {
                System.out.print(", ");
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
            return;
        }

        Long InitialTime = System.nanoTime();

        quickSort(list, 0, list.getLength() - 1);

        Long FinalTime = System.nanoTime();
        Long Duration = FinalTime - InitialTime;

        PrintResult(list, "Lista ordenada");
        System.out.println("\nTiempo de ejecución QuickSort: " + Duration + " ns");
    }

    public static void main(String[] args) {
        QuickSort app = new QuickSort();
        app.DataProcess();
    }
}
