/**
* Clase EntradaSalidaFicherosCSV.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.Espectaculo;

public class EntradaSalidaFicherosCSV {

	// La ruta es el archivo y el separador es lo que separa las columnas ej","
	public static List<String[]> leerCsv(String ruta, String separador,
			boolean saltarCabecera) throws IOException {

		List<String[]> filas = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

			String linea;

			// Si hay cabecera y la quieres saltar
			if (saltarCabecera) {
				br.readLine(); // descarta la primera línea
			}

			while ((linea = br.readLine()) != null) {
				// Ignorar líneas vacías
				if (linea.isBlank()) {
					continue;
				}

				// Dividir en columnas
				String[] partes = linea.split(separador, -1);
				// -1 => no descarta columnas vacías al final

				// Trim de cada campo
				for (int i = 0; i < partes.length; i++) {
					partes[i] = partes[i].trim();
				}

				filas.add(partes);
			}
		}

		return filas;
	}

	// Pasamos la ruta, la lista de filas y el separador a introducir.
	public static void escribirCsv(String ruta, List<String[]> filas,
			String separador) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
			for (String[] fila : filas) {
				String linea = String.join(separador, fila);
				bw.write(linea);
				bw.newLine();
			}
		}
	}

}
