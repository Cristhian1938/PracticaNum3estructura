package unl.dance.base.controller.Practica2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Arreglos {

    private BufferedReader FileReadM(String fileName) throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new Exception("Archivo '" + fileName + "' no encontrado en recursos");
        }
        return new BufferedReader(new InputStreamReader(input));
    }

    public Integer countLin() {
        Integer cantidad = 0;
        try (BufferedReader br = FileReadM("data.txt")) {
            while (br.readLine() != null) {
                cantidad++;
            }
        } catch (Exception e) {
            System.out.println("Archivo no encontrado " + e.getMessage());
        }
        return cantidad;
    }

    public void DataProcess() {
        Long InitialTime = System.nanoTime();
        Integer cantidadLineas = countLin();
        Integer[] arreglo = new Integer[cantidadLineas];
        int i = 0;

        try (BufferedReader br = FileReadM("data.txt")) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    arreglo[i] = Integer.parseInt(linea.trim());
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        Integer[] contador = new Integer[cantidadLineas];
        for (int j = 0; j < cantidadLineas; j++) {
            contador[j] = 0;
        }

        for (int j = 0; j < arreglo.length; j++) {
            for (int k = j + 1; k < arreglo.length; k++) {
                if (arreglo[j] != null && arreglo[j].equals(arreglo[k])) {
                    contador[j]++;
                    arreglo[k] = null;
                }
            }
        }

        int numRepeated = 0;
        for (int j = 0; j < contador.length; j++) {
            if (contador[j] > 0) {
                numRepeated++;
            }
        }

        Integer[] repetidos = new Integer[numRepeated];
        int index = 0;
        for (int j = 0; j < contador.length; j++) {
            if (contador[j] > 0) {
                repetidos[index++] = arreglo[j];
            }
        }

        Long FinalTime = System.nanoTime();
        Long Duration = FinalTime - InitialTime;
 
        PrintResult(repetidos, "Elementos repetidos");
        System.out.println("\nCantidad de elementos repetidos: " + numRepeated);
        System.out.println("Tiempo de ejecución: " + Duration + " ns");
    }

    public void PrintResult(Integer[] arreglo, String titulo) {
        System.out.println(titulo + ":");
        for (int i = 0; i < arreglo.length; i++) {
            System.out.print(arreglo[i]);
            if (i < arreglo.length - 1) {
                System.out.print(", ");
            }
        }
    }

    public static void main(String[] args) {
        Arreglos app = new Arreglos();
        app.DataProcess();
    }
}
