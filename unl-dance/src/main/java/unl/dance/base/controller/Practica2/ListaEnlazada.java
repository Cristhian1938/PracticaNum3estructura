package unl.dance.base.controller.Practica2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import unl.dance.base.controller.data_struct.list.LinkedList;

public class ListaEnlazada {

    private BufferedReader FileReadM(String fileName) throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new Exception("Archivo '" + fileName + "' no encontrado en recursos");
        }
        return new BufferedReader(new InputStreamReader(input));
    }

    public void DataProcess() {
        long initialTime = System.nanoTime();

        LinkedList<Integer> lista = new LinkedList<>();

        try (BufferedReader br = FileReadM("data.txt")) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lista.add(Integer.parseInt(linea.trim()));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo data: " + e.getMessage());
            return;
        }

        int numRepeated = countRepeated(lista);
        long finalTime = System.nanoTime();
        long duration = finalTime - initialTime;

        PrintResult(lista, "Elementos en la lista");
        System.out.println("Cantidad de elementos repetidos: " + numRepeated);
        System.out.println("Tiempo de ejecución: " + duration + " ns");
    }

    
    public int countRepeated(LinkedList<Integer> lista) {
        HashMap<Integer, Integer> occurrences = new HashMap<>();
        for (int i = 0; i < lista.getLength(); i++) {
            Integer value = lista.get(i);
            occurrences.put(value, occurrences.getOrDefault(value, 0) + 1);
        }
        int repeatedCount = 0;
        for (int count : occurrences.values()) {
            if (count > 1) {
                repeatedCount++;
            }
        }
        return repeatedCount;
    }

    public void PrintResult(LinkedList<Integer> list, String titulo) {
        System.out.println(titulo + ":");
        System.out.print(list.print());
    }
    
    public static void main(String[] args) {
        ListaEnlazada app = new ListaEnlazada();
        app.DataProcess();
    }
}