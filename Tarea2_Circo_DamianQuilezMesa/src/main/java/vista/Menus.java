/**
* Clase Menus.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package vista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import controlador.ServicioCredenciales;
import controlador.ServicioEspectaculo;
import dao.CredencialesDAO;
import modelo.Credenciales;
import modelo.Espectaculo;
import modelo.Perfiles;
import modelo.Sesion;

public class Menus {

	static Sesion sesion = new Sesion();
	static Scanner sc = new Scanner(System.in);

	public static void selectorMenu(Sesion sesion) {
		// En funcion del perfil selecciona su correspondiente menu
		switch (sesion.getPerfil()) {
		case INVITADO:
			menuInvitado(sesion);
			break;
		case ADMIN:
			menuAdmin(sesion);
			break;
		case ARTISTA:
			menuArtista(sesion);
			break;
		case COORDINACION:
			menuCoordinacion(sesion);
			break;
		default:
			System.out.println("Sesion inválida");
			break;
		}
	}

	public static void menuInvitado(Sesion sesion) {
		String opcionMenu = "";

		// Mostramos el menú de opciones
		do {
			System.out.println(
					"Hola " + Perfiles.INVITADO + ", selecciona una opción");
			System.out.println("1-Ver espectáculos");
			System.out.println("2-Log in");
			System.out.println("3-Salir del programa");
			// Seleccionamos la opcion
			opcionMenu = sc.nextLine();

			// Elegimos la tarea a realizar
			switch (opcionMenu) {
			case "1":
				mostrarEspectaculos();
				break;
			case "2":
				logIn(sesion);
				System.out.println("Punto 1");
				break;
			case "3":
				// Solo salimos del menú una vez se elija la opción 3 ya que
				// esta establece la sesión a null
				System.out.println("Saliendo del programa");
				sesion.setPerfil(null);
				break;

			default:
				System.out.println("Opción no valida, vuelve a intentarlo");
				break;
			}
			System.out.println("Punto 2");
			// Mientras que se sea invitado sigue en el menú, la única
			// casuística en la que se sale es cuando establecemos a null el
			// perfil siendo en todo el programa posible unicamente en este menú
			// al elegir la opcion 3
		} while (sesion.getPerfil() == (Perfiles.INVITADO));

	}

	public static void logIn(Sesion sesion) {
	    ServicioCredenciales servicio = new ServicioCredenciales();
	    String nombreUsuario;
	    String contrasena;
	    boolean autenticado = false;

	    do {
	    	nombreUsuario=pedirNombreUsuario();
	    	contrasena=pedirContrasena();


	        autenticado = servicio.servicioLogin(sesion, nombreUsuario, contrasena);

	        if (!autenticado) {
	            System.out.println("Credenciales incorrectas. Vuelve a intentarlo.");
	        }
	    } while (!autenticado);
	    System.out.println("Login correcto!");
	    selectorMenu(sesion);
	}


	private static String pedirNombreUsuario() {
	    String nomusuario;
	    do {
	        System.out.print("Introduce el usuario o escribe 'salir' para cancelar el login: ");
	        nomusuario = sc.nextLine().trim();

	        if (nomusuario.equalsIgnoreCase("salir")) {
	            System.out.println("Has cancelado el login.");
	            return "SALIR"; // valor especial
	        }

	        if (nomusuario.isBlank()) {
	            System.out.println("El usuario no puede estar vacío.");
	        }
	    } while (nomusuario.isBlank());

	    return nomusuario;
	}



	private static String pedirContrasena() {
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



	public static boolean verificarLoginPerfiles(String usuario, String contrasena, Sesion sesion) {
	    boolean credencialesCorrectas = false;
	    CredencialesDAO credencialesDAO = new CredencialesDAO();

	    // Obtenemos todas las credenciales de la BD
	    Set<Credenciales> listaCredenciales = credencialesDAO.obtenerCredenciales();

	    for (Credenciales c : listaCredenciales) {
	        // Comprobamos coincidencia de usuario y contraseña
	        if (c.getNombreUsuario().equalsIgnoreCase(usuario.trim())
	                && c.getPassword().equals(contrasena.trim())) {

	            // Según el perfil asignamos la sesión
	            switch (c.getPerfil()) {
	                case ARTISTA:
	                    System.out.println("Usuario autenticado como ARTISTA");
	                    sesion.setCredenciales(c);
	                    sesion.setPerfil(Perfiles.ARTISTA);
	                    credencialesCorrectas = true;
	                    break;
	                case COORDINACION:
	                    System.out.println("Usuario autenticado como COORDINACIÓN");
	                    sesion.setCredenciales(c);
	                    sesion.setPerfil(Perfiles.COORDINACION);
	                    credencialesCorrectas = true;
	                    break;
	                default:
	                    System.out.println("Perfil no reconocido: " + c.getPerfil());
	                    credencialesCorrectas = false;
	            }
	        }
	    }

	    if (!credencialesCorrectas) {
	        System.out.println("Credenciales incorrectas. Vuelve a intentarlo.");
	    }

	    return credencialesCorrectas;
	}


	public static boolean verificarLoginAdmin(String nomusuario,
			String contrasenia, Sesion sesion) {
		boolean credencialesCorrectas = false;

		// Cargamos el application.properties para extraer el usuario
		// y contraseña de admin
		Properties propiedades = new Properties();
		// Podria pedir manualmente la ruta si no carga correctamente
		try {
			propiedades.load(new FileInputStream(
					"src/main/resources/application.properties"));
		} catch (FileNotFoundException e) {
			System.out.println("No se encuentra el fichero");
			credencialesCorrectas = false;
		} catch (IOException e) {
			System.out.println("El formatin ye incorrectu");
			credencialesCorrectas = false;
		}

		/*
		 * Almacenamos en un string el string asociado a usuarioAdmin y a
		 * passwordAdmin que en este caso es admin para ambos
		 */
		String usuarioAdmin = propiedades.getProperty("usuarioAdmin");
		String contraseñaAdmin = propiedades.getProperty("passwordAdmin");

		/*
		 * Comprobamos que las credenciales introducidas por teclado coincidan
		 * con las de admin, si es asi le establecemos el tipo de perfil a ADMIN
		 * y devolvemos true, si no false
		 */
		if (nomusuario.equals(usuarioAdmin)
				&& contrasenia.equals(contraseñaAdmin)) {
			System.out.println("Bienvenido Administrador!");
			sesion.setPerfil(Perfiles.ADMIN);
			credencialesCorrectas = true;
		} else {
			credencialesCorrectas = false;
		}
		return credencialesCorrectas;
	}

	public static void mostrarEspectaculos() {
		ServicioEspectaculo servicio = new ServicioEspectaculo();
		List<Espectaculo> espectaculos = servicio.listarEspectaculosBasico();

		System.out.println("=== Lista de Espectáculos ===");
		if (espectaculos.isEmpty()) {
			System.out.println("No hay espectáculos disponibles.");
		} else {
			for (Espectaculo e : espectaculos) {
				System.out.println(e); // usa el toString() de Espectaculo
			}
		}
	}

	public static void menuAdmin(Sesion sesion) {
		String opcionMenu = "";

		System.out.println("Hola " + modelo.Perfiles.ADMIN + ", bienvenido.");

		// Mostramos el menú
		do {
			System.out.println("-----SELECCIONA UNA OPCION-----");
			System.out.println("1 - Gestionar espectáculos");
			System.out.println("2 - Gestionar personas");
			System.out.println("3 - Cerrar sesión");
			// Introducimos la opcion
			opcionMenu = sc.nextLine();

			// Seleccionamos entre las diferentes opciones y ejecutamos el
			// método
			switch (opcionMenu) {
			case "1":
				//gestionarEspectaculos();
				break;
			case "2":
				// gestionarPersonas();
				break;
			case "3":
				sesion.cerrarSesion();
				break;

			default:
				System.out.println("Opción no valida, vuelve a intentarlo");
				break;
			}
			// Mientras que el usuario no quiera salir, se repite el menú
		} while (!opcionMenu.equals("3"));

	}
	
	public static void registrarPersona(Sesion sesion) {
	    if (sesion.getPerfil() != Perfiles.ADMIN) {
	        System.out.println("Acceso denegado. Solo Admin puede registrar personas.");
	        return;
	    }

	    System.out.print("Nombre de la persona: ");
	    String nombre = sc.nextLine().trim();
	    
	    System.out.print("Primer apellido: ");
	    String nombre = sc.nextLine().trim();
	    
	    System.out.print("Segundo apellido: ");
	    String nombre = sc.nextLine().trim();

	    System.out.print("Email: ");
	    String email = sc.nextLine().trim();

	    System.out.print("Nacionalidad: ");
	    String nacionalidad = sc.nextLine().trim();

	    System.out.print("Rol (COORDINACION/ARTISTA): ");
	    String rol = sc.nextLine().trim();

	    System.out.print("Seniority: ");
	    String seniority = sc.nextLine().trim();

	    System.out.print("Apodo: ");
	    String apodo = sc.nextLine().trim();

	    System.out.print("Especialidad: ");
	    String especialidad = sc.nextLine().trim();

	    System.out.print("Nombre de usuario: ");
	    String usuario = sc.nextLine().trim();

	    System.out.print("Contraseña: ");
	    String password = sc.nextLine().trim();

	    Persona persona = new Persona(nombre, email, nacionalidad, rol, seniority, apodo, especialidad);
	    Credenciales cred = new Credenciales(null, usuario, password, Perfiles.valueOf(rol.toUpperCase()));

	    ServicioPersona servicio = new ServicioPersona();
	    boolean ok = servicio.registrarPersona(persona, cred);

	    if (ok) {
	        System.out.println("Persona registrada correctamente.");
	    } else {
	        System.out.println("Error en el registro. Revisa los datos.");
	    }
	}


	public static void menuArtista(Sesion sesion) {
		String opcionMenu = "";
		// Mostramos el menú
		do {
			System.out.println("Hola " + sesion.credenciales.getNombreUsuario()
					+ ", selecciona una opción");
			System.out.println("1-Ver espectáculos");
			System.out.println("3-Cerrar sesión");
			// Selecionamos la opción a realizar
			opcionMenu = sc.nextLine();

			// Seleccionamos la opción a llevar a cabo
			switch (opcionMenu) {
			case "1":
				mostrarEspectaculos();
				break;
			case "2":
				sesion.cerrarSesion();
				break;
			default:
				System.out.println("Opción no valida, vuelve a intentarlo");
				break;
			}
		} while (!opcionMenu.equals("2"));

	}

	public static void menuCoordinacion(Sesion sesion) {
		String opcionMenu = "";
		// Mostramos el menú
		do {
			System.out.println("Hola " + sesion.credenciales.getNombreUsuario()
					+ ", selecciona una opción");
			System.out.println("1-Gestionar espectáculos");
			System.out.println("2-Cerrar sesión");
			// Selecionamos la opción a realizar
			opcionMenu = sc.nextLine();

			// Seleccionamos la opción a llevar a cabo
			switch (opcionMenu) {
			case "1":
				// gestionarEspectaculos();
				break;
			case "2":
				sesion.cerrarSesion();
				break;
			default:
				System.out.println("Opción no valida, vuelve a intentarlo");
				break;
			}
		} while (!opcionMenu.equals("2"));

	}

}
