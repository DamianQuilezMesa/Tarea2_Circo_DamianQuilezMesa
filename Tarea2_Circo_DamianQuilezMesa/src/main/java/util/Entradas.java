/**
* Clase Entradas.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Entradas {

	static Scanner sc = new Scanner(System.in);

	public static String pedirOpcionMenu(String prompt, int maxOpcion) {
		String opcion;
		do {
			System.out.println(prompt);
			opcion = sc.nextLine().trim();
			try {
				int valor = Integer.parseInt(opcion);
				if (valor >= 1 && valor <= maxOpcion) {
					return opcion;
				}
			} catch (NumberFormatException ignored) {
			}
			System.out.println("Opción inválida, inténtalo de nuevo.");
		} while (true);
	}

	public static String pedirNombreUsuario() {
		String nomusuario;
		do {
			System.out.print(
					"Introduce el usuario o escribe 'salir' para cancelar el login: ");
			nomusuario = sc.nextLine().trim();

			if (nomusuario.equalsIgnoreCase("salir")) {
				return "SALIR";
			}
			if (nomusuario.isBlank()) {
				System.out.println("El usuario no puede estar vacío.");
			}
		} while (nomusuario.isBlank());

		return nomusuario;
	}

	public static String pedirContrasena() {
		String contrasena;
		do {
			System.out.print("Contraseña: ");
			contrasena = sc.nextLine().trim();
			if (contrasena.isBlank()) {
				System.out.println("La contraseña no puede estar vacía.");
			}
		} while (contrasena.isBlank());
		return contrasena;
	}

	public static LocalDate leerFechaSenior(Scanner sc) {
		LocalDate fecha = null;
		boolean valida = false;
		while (!valida) {
			System.out.print("Fecha senior (YYYY-MM-DD): ");
			String input = sc.nextLine().trim();
			try {
				fecha = LocalDate.parse(input);
				valida = true;
			} catch (DateTimeParseException e) {
				System.out.println(
						"Formato de fecha inválido. Inténtalo de nuevo.");
			}
		}
		return fecha;
	}

	public static LocalDate leerFecha(String prompt) {
		while (true) {
			System.out.print(prompt);
			String input = sc.nextLine().trim();
			try {
				return LocalDate.parse(input);
			} catch (Exception e) {
				System.out.println("Formato inválido.");
			}
		}
	}

}
