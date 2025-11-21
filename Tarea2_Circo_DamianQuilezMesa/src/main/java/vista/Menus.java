package vista;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import controlador.ServicioArtista;
import controlador.ServicioCredenciales;
import controlador.ServicioEspectaculo;
import controlador.ServicioPersona;
import dao.ArtistaDAO;
import dao.CoordinacionDAO;
import dao.CredencialesDAO;
import dao.EspectaculoDAO;
import dao.PersonaDAO;
import dto.ArtistaDTO;
import dto.CoordinacionDTO;
import dto.EspectaculoDTO;
import dto.FichaArtistaDTO;
import dto.InformeEspectaculo;
import dto.NumeroDTO;
import dto.PersonaDTO;
import modelo.Artista;
import modelo.Coordinacion;
import modelo.Credenciales;
import modelo.Especialidad;
import modelo.Perfiles;
import modelo.Sesion;
import util.Validadores;
import util.Entradas;

public class Menus {

    static Sesion sesion = new Sesion();
    static Scanner sc = new Scanner(System.in);

    public static void selectorMenu(Sesion sesion) {
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
                System.out.println("Sesión inválida.");
                break;
        }
    }
    

    public static void menuInvitado(Sesion sesion) {
        String opcion = "";
        do {
            System.out.println("Hola " + Perfiles.INVITADO + ", selecciona una opción");
            System.out.println("1 - Ver espectáculos");
            System.out.println("2 - Log in");
            System.out.println("3 - Salir del programa");
            opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                    mostrarEspectaculosBasico();
                    break;
                case "2":
                    logIn(sesion);
                    break;
                case "3":
                    System.out.println("Saliendo del programa.");
                    sesion.setPerfil(null);
                    break;
                default:
                    System.out.println("Opción no válida, vuelve a intentarlo.");
            }
        } while (sesion.getPerfil() == Perfiles.INVITADO);
    }

    public static void logIn(Sesion sesion) {
        ServicioCredenciales servicio = new ServicioCredenciales();
        String nombreUsuario;
        String contrasena;
        boolean autenticado = false;

        do {
            nombreUsuario = Entradas.pedirNombreUsuario();
            if ("SALIR".equals(nombreUsuario)) {
                System.out.println("Has cancelado el login.");
                return;
            }
            contrasena = Entradas.pedirContrasena();
            autenticado = servicio.login(sesion, nombreUsuario, contrasena);

            if (!autenticado) {
                System.out.println("Credenciales incorrectas. Vuelve a intentarlo.");
            }
        } while (!autenticado);

        System.out.println("Login correcto!");
        selectorMenu(sesion);
    }

    

    public static boolean verificarLoginPerfiles(String usuario, String contrasena, Sesion sesion) {
        boolean credencialesCorrectas = false;
        CredencialesDAO credencialesDAO = new CredencialesDAO();
        Set<Credenciales> listaCredenciales = credencialesDAO.obtenerCredenciales();

        for (Credenciales c : listaCredenciales) {
            if (c.getNomUsuario().equalsIgnoreCase(usuario.trim())
                    && c.getContrasena().equals(contrasena.trim())) {
                switch (c.getPerfil()) {
                    case ARTISTA:
                        sesion.setCredenciales(c);
                        sesion.setPerfil(Perfiles.ARTISTA);
                        credencialesCorrectas = true;
                        break;
                    case COORDINACION:
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

    public static boolean verificarLoginAdmin(String nomusuario, String contrasenia, Sesion sesion) {
        boolean credencialesCorrectas = false;
        Properties propiedades = new Properties();
        try {
            propiedades.load(new FileInputStream("src/main/resources/application.properties"));
        } catch (FileNotFoundException e) {
            System.out.println("No se encuentra el fichero.");
            return false;
        } catch (IOException e) {
            System.out.println("Formato incorrecto en properties.");
            return false;
        }

        String usuarioAdmin = propiedades.getProperty("usuarioAdmin");
        String contraseñaAdmin = propiedades.getProperty("passwordAdmin");

        if (nomusuario.equals(usuarioAdmin) && contrasenia.equals(contraseñaAdmin)) {
            System.out.println("Bienvenido Administrador!");
            sesion.setPerfil(Perfiles.ADMIN);
            credencialesCorrectas = true;
        }
        return credencialesCorrectas;
    }

    public static void menuAdmin(Sesion sesion) {
        String opcion = "";
        System.out.println("Hola " + Perfiles.ADMIN + ", bienvenido.");
        do {
            System.out.println("----- SELECCIONA UNA OPCIÓN -----");
            System.out.println("1 - Ver espectáculos");
            System.out.println("2 - Gestionar espectáculos");
            System.out.println("3 - Gestionar personas");
            System.out.println("4 - Cerrar sesión");
            opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                    mostrarEspectaculosCompleto();
                    break;
                case "2":
                    gestionarEspectaculos(sesion);
                    break;
                case "3":
                    gestionarPersonasAdmin(sesion);
                    break;
                case "4":
                    sesion.cerrarSesion();
                    break;
                default:
                    System.out.println("Opción no válida, vuelve a intentarlo.");
            }
        } while (!opcion.equals("4"));
    }

    private static void menuArtista(Sesion sesion) {
        ServicioArtista servicioArtista = new ServicioArtista();
        String opcionMenu;

        System.out.println("Hola " + sesion.getCredenciales().getNomUsuario() + ", bienvenido.");
        do {
            System.out.println("\n=== MENÚ ARTISTA ===");
            System.out.println("1 - Ver espectáculo completo");
            System.out.println("2 - Ver mi ficha");
            System.out.println("3 - Cerrar sesión");

            opcionMenu = Entradas.pedirOpcionMenu("Selecciona una opción:", 3);

            switch (opcionMenu) {
                case "1":
                    mostrarEspectaculosCompleto();
                    break;
                case "2":
                    verFichaArtistaVista(servicioArtista, sesion);
                    break;
                case "3":
                    sesion.cerrarSesion();
                    System.out.println("Sesión cerrada.");
                    break;
            }
        } while (!opcionMenu.equals("3"));
    }


    public static void menuCoordinacion(Sesion sesion) {
        String opcion = "";
        System.out.println("Hola " + sesion.getCredenciales().getNomUsuario() + ", selecciona una opción");
        do {
            System.out.println("1 - Ver espectáculos");
            System.out.println("2 - Gestionar espectáculos");
            System.out.println("3 - Cerrar sesión");
            opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                    mostrarEspectaculosCompleto();
                    break;
                case "2":
                    gestionarEspectaculos(sesion);
                    break;
                case "3":
                    sesion.cerrarSesion();
                    break;
                default:
                    System.out.println("Opción no válida, vuelve a intentarlo.");
            }
        } while (!opcion.equals("3"));
    }

    public static void gestionarPersonasAdmin(Sesion sesion) {
        String opcion = "";
        do {
            System.out.println("----- SELECCIONA UNA OPCIÓN -----");
            System.out.println("1 - Registrar una nueva persona");
            System.out.println("2 - Gestionar personas ya registradas");
            System.out.println("3 - Salir de gestión de personas");
            opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                    registrarPersona(sesion);
                    break;
                case "2":
                    modificarPersonaPorUsuario();
                    break;
                case "3":
                    break;
                default:
                    System.out.println("Opción no válida, vuelve a intentarlo.");
            }
        } while (!opcion.equals("3"));
    }

    public static void registrarPersona(Sesion sesion) {
        // Datos personales
        System.out.print("Nombre de la persona: ");
        String nombre = sc.nextLine().trim();
        while (!Validadores.esNombreValido(nombre)) {
            System.out.println("Error: el nombre solo puede contener letras y espacios.");
            nombre = sc.nextLine().trim();
        }

        System.out.print("Primer apellido: ");
        String apellido1 = sc.nextLine().trim();
        while (!Validadores.esNombreValido(apellido1)) {
            System.out.println("Error: el primer apellido solo puede contener letras y espacios.");
            apellido1 = sc.nextLine().trim();
        }

        System.out.print("Segundo apellido: ");
        String apellido2 = sc.nextLine().trim();
        while (!Validadores.esNombreValido(apellido2)) {
            System.out.println("Error: el segundo apellido solo puede contener letras y espacios.");
            apellido2 = sc.nextLine().trim();
        }

        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        while (!Validadores.esEmailValido(email)) {
            System.out.println("Error: el email debe tener el formato x@x.xx");
            email = sc.nextLine().trim();
        }

        // Nacionalidad
        String pais = seleccionarNacionalidad();

        // Perfil
        Perfiles perfil = null;
        do {
            System.out.println("Selecciona el perfil:");
            System.out.println("1 - Coordinación");
            System.out.println("2 - Artista");
            String selector = sc.nextLine().trim();
            if ("1".equals(selector)) perfil = Perfiles.COORDINACION;
            else if ("2".equals(selector)) perfil = Perfiles.ARTISTA;
            else System.out.println("Opción incorrecta, inténtalo de nuevo.");
        } while (perfil == null);

        // Usuario y contraseña
        System.out.print("Nombre de usuario: ");
        String nomUsuario = sc.nextLine().trim().toLowerCase();
        while (!Validadores.esNombreValido(nomUsuario)) {
            System.out.println("Error: nombre de usuario inválido.");
            nomUsuario = sc.nextLine().trim().toLowerCase();
        }

        System.out.print("Contraseña: ");
        String contrasena = sc.nextLine().trim();
        while (!Validadores.esContrasenaValida(contrasena)) {
            System.out.println("Error: contraseña inválida.");
            contrasena = sc.nextLine().trim();
        }

        // Comprobaciones en BD
        PersonaDAO personaDAO = new PersonaDAO();
        if (personaDAO.emailExists(email)) {
            System.out.println("Error: el email ya está registrado.");
            return;
        }
        if (personaDAO.usuarioExists(nomUsuario)) {
            System.out.println("Error: el nombre de usuario ya está registrado.");
            return;
        }

        // Datos específicos por perfil
        Coordinacion coord = null;
        Artista art = null;

        if (perfil == Perfiles.COORDINACION) {
            boolean senior = false;
            LocalDate fechaSenior = null;

            boolean opcionOk = false;
            do {
                System.out.println("¿Es senior?");
                System.out.println("1 - Sí");
                System.out.println("2 - No");
                System.out.println("3 - Cancelar");
                String o = sc.nextLine().trim();
                switch (o) {
                    case "1":
                        senior = true;
                        // Fecha senior
                        boolean fechaValida = false;
                        while (!fechaValida) {
                            System.out.print("Fecha senior (YYYY-MM-DD): ");
                            String input = sc.nextLine().trim();
                            try {
                                fechaSenior = LocalDate.parse(input);
                                fechaValida = true;
                            } catch (DateTimeParseException e) {
                                System.out.println("Formato de fecha inválido. Inténtalo de nuevo.");
                            }
                        }
                        opcionOk = true;
                        break;
                    case "2":
                        senior = false;
                        fechaSenior = null;
                        opcionOk = true;
                        break;
                    case "3":
                        System.out.println("Registro cancelado.");
                        return;
                    default:
                        System.out.println("Opción incorrecta, inténtalo de nuevo.");
                }
            } while (!opcionOk);

            coord = new Coordinacion();
            coord.setSenior(senior);
            coord.setFechasenior(fechaSenior);

        } else if (perfil == Perfiles.ARTISTA) {
            System.out.print("Apodo: ");
            String apodo = sc.nextLine().trim();

            // Menú de especialidades
            List<String> opciones = new ArrayList<>(Arrays.asList(
                    "ACROBACIA", "HUMOR", "MAGIA", "EQUILIBRISMO", "MALABARISMO"
            ));
            ArrayList<Especialidad> especialidades = new ArrayList<>();

            System.out.println("=== Selección de Especialidades ===");
            System.out.println("Debes elegir al menos 1 y como máximo 5.");

            while (especialidades.size() < 5 && !opciones.isEmpty()) {
                System.out.println("\nOpciones disponibles:");
                for (int i = 0; i < opciones.size(); i++) {
                    System.out.println((i + 1) + " - " + opciones.get(i));
                }
                System.out.println("0 - Terminar selección");

                System.out.print("Elige una opción: ");
                String input = sc.nextLine().trim();
                int opcion;
                try {
                    opcion = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Intenta de nuevo.");
                    continue;
                }

                if (opcion == 0) {
                    if (especialidades.isEmpty()) {
                        System.out.println("Debes elegir al menos una especialidad.");
                    } else {
                        break; // salir si ya hay al menos una
                    }
                } else if (opcion >= 1 && opcion <= opciones.size()) {
                    String elegida = opciones.remove(opcion - 1);
                    especialidades.add(Especialidad.valueOf(elegida));
                    System.out.println("Has elegido: " + elegida);
                } else {
                    System.out.println("Opción no válida.");
                }
            }

            art = new Artista();
            art.setApodo(apodo);
            art.setEspecialidades(especialidades);
        }


        // DTO y servicio
        PersonaDTO persona = new PersonaDTO(nombre, apellido1, apellido2, email, pais, nomUsuario, contrasena, perfil);
        ServicioPersona servicio = new ServicioPersona();
        boolean creado = servicio.registrarPersona(persona, art, coord);

        if (creado) {
            System.out.println("Persona registrada correctamente.");
        } else {
            System.out.println("Error en el registro.");
        }
    }

    public static void modificarPersonaPorUsuario() {
        PersonaDAO personaDAO = new PersonaDAO();
        ArtistaDAO artistaDAO = new ArtistaDAO();
        CoordinacionDAO coordinacionDAO = new CoordinacionDAO();
        ServicioPersona servicio = new ServicioPersona();

        String usuario = "";
        PersonaDTO persona = null;
        do {
            System.out.print("Introduce el nombre de usuario de la persona a modificar (o 'salir' para cancelar): ");
            usuario = sc.nextLine().trim();
            if (usuario.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada.");
                return;
            }
            persona = personaDAO.obtenerPorUsuario(usuario);
            if (persona == null) {
                System.out.println("No existe ninguna persona con ese usuario. Inténtalo de nuevo.");
            }
        } while (persona == null);

        System.out.println("=== Datos actuales ===");
        System.out.println("Nombre: " + persona.getNombre());
        System.out.println("Email: " + persona.getEmail());
        System.out.println("Nacionalidad: " + persona.getPais());
        System.out.println("Perfil: " + persona.getPerfil());

        String nuevoNombre;
        while (true) {
            System.out.print("Nuevo nombre (Enter para mantener): ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                nuevoNombre = persona.getNombre();
                break;
            }
            if (Validadores.esNombreValido(input)) {
                nuevoNombre = input;
                break;
            }
            System.out.println("Error: el nombre solo puede contener letras y espacios (mínimo 2 caracteres).");
        }

        String nuevoEmail;
        while (true) {
            System.out.print("Nuevo email (formato x@x.xx, Enter para mantener): ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                nuevoEmail = persona.getEmail();
                break;
            }
            if (Validadores.esEmailValido(input)) {
                nuevoEmail = input;
                break;
            }
            System.out.println("Error: el email debe tener el formato x@x.xx");
        }

        String nuevoPais = seleccionarNacionalidad();

        Artista art = null;
        Coordinacion coord = null;

        if (persona.getPerfil() == Perfiles.ARTISTA) {
            art = artistaDAO.obtenerPorIdPersona(persona.getId());
            if (art == null) art = new Artista();

            System.out.println("=== Datos de Artista ===");
            System.out.println("Apodo actual: " + (art.getApodo() != null ? art.getApodo() : "(sin apodo)"));
            System.out.println("Especialidades actuales: " + (art.getEspecialidades() != null ? art.getEspecialidades() : "[]"));

            System.out.print("Nuevo apodo (Enter para mantener): ");
            String apodoInput = sc.nextLine().trim();
            String nuevoApodo = apodoInput.isEmpty() ? art.getApodo() : apodoInput;

            System.out.print("Nuevas especialidades (coma; Enter para mantener): ");
            String espInput = sc.nextLine().trim();
            ArrayList<Especialidad> nuevasEsp;
            if (espInput.isEmpty()) {
                nuevasEsp = art.getEspecialidades();
            } else {
                nuevasEsp = parsearEspecialidades(espInput);
                if (nuevasEsp == null) {
                    System.out.println("Error: alguna especialidad no es válida. Operación cancelada.");
                    return;
                }
            }

            Artista artistaActualizado = new Artista();
            artistaActualizado.setApodo(nuevoApodo);
            artistaActualizado.setEspecialidades(nuevasEsp);
            art = artistaActualizado;

        } else if (persona.getPerfil() == Perfiles.COORDINACION) {
            coord = coordinacionDAO.obtenerPorIdPersona(persona.getId());
            if (coord == null) coord = new Coordinacion();

            System.out.println("=== Datos de Coordinación ===");
            System.out.println("Es senior: " + (coord.getSenior() ? "Sí" : "No"));
            System.out.println("Fecha senior: " + (coord.getFechasenior() != null ? coord.getFechasenior() : "(sin fecha)"));

            boolean opcionValida = false;
            boolean nuevoSenior = coord.getSenior();
            LocalDate nuevaFechaSenior = coord.getFechasenior();

            do {
                System.out.println("¿Es senior?");
                System.out.println("1 - Sí");
                System.out.println("2 - No");
                System.out.println("3 - Mantener");
                System.out.println("4 - Cancelar");
                String op = sc.nextLine().trim();

                switch (op) {
                    case "1":
                        nuevoSenior = true;
                        nuevaFechaSenior = Entradas.leerFechaSenior(sc);
                        opcionValida = true;
                        break;
                    case "2":
                        nuevoSenior = false;
                        nuevaFechaSenior = null;
                        opcionValida = true;
                        break;
                    case "3":
                        opcionValida = true;
                        break;
                    case "4":
                        System.out.println("Operación cancelada.");
                        return;
                    default:
                        System.out.println("Opción incorrecta, inténtalo de nuevo.");
                }
            } while (!opcionValida);

            Coordinacion coordActualizada = new Coordinacion();
            coordActualizada.setSenior(nuevoSenior);
            coordActualizada.setFechasenior(nuevaFechaSenior);
            coord = coordActualizada;
        }

        PersonaDTO personaActualizada = new PersonaDTO(
            persona.getId(),
            nuevoNombre,
            persona.getApellido1(),
            persona.getApellido2(),
            nuevoEmail,
            nuevoPais,
            persona.getNomUsuario(),
            null,
            persona.getPerfil()
        );

        boolean actualizado = servicio.actualizarPersona(personaActualizada, art, coord);
        if (actualizado) {
            System.out.println("Datos actualizados correctamente.");
        } else {
            System.out.println("Error al actualizar los datos.");
        }
    }

    public static void mostrarEspectaculosBasico() {
        ServicioEspectaculo servicio = new ServicioEspectaculo();
        List<EspectaculoDTO> espectaculos = servicio.obtenerTodosLosEspectaculos();

        System.out.println("=== Lista de Espectáculos ===");
        if (espectaculos.isEmpty()) {
            System.out.println("No hay espectáculos disponibles.");
        } else {
            for (EspectaculoDTO e : espectaculos) {
                System.out.println(e);
            }
        }
    }

    public static void mostrarEspectaculosCompleto() {
        ServicioEspectaculo servicio = new ServicioEspectaculo();

        List<EspectaculoDTO> espectaculos = servicio.obtenerTodosLosEspectaculos();
        if (espectaculos.isEmpty()) {
            System.out.println("No hay espectáculos registrados.");
            return;
        }

        System.out.println("=== Espectáculos disponibles ===");
        for (int i = 0; i < espectaculos.size(); i++) {
            EspectaculoDTO e = espectaculos.get(i);
            System.out.printf("%d - %s (%s a %s)%n", i + 1, e.getNombreEsp(), e.getFechaIni(), e.getFechaFin());
        }

        int opcion = -1;
        do {
            System.out.print("Selecciona un espectáculo por número: ");
            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = -1;
            }
        } while (opcion < 1 || opcion > espectaculos.size());

        EspectaculoDTO seleccionado = espectaculos.get(opcion - 1);
        InformeEspectaculo informe = servicio.generarInformeEspectaculo(seleccionado.getIdEspectaculo());

        System.out.println("\n=== INFORME DE ESPECTÁCULO ===");
        System.out.println("ID: " + informe.getEspectaculo().getIdEspectaculo());
        System.out.println("Nombre: " + informe.getEspectaculo().getNombreEsp());
        System.out.println("Fechas: " + informe.getEspectaculo().getFechaIni() + " a " + informe.getEspectaculo().getFechaFin());

        CoordinacionDTO coord = informe.getCoordinador();
        System.out.println("\n--- Coordinador ---");
        if (coord != null) {
            System.out.println("Nombre: " + coord.getNombre());
            System.out.println("Email: " + coord.getEmail());
            System.out.println("¿Senior?: " + (coord.isSenior() ? "Sí" : "No"));
        } else {
            System.out.println("No hay coordinador asignado.");
        }

        System.out.println("\n--- Números ---");
        for (NumeroDTO num : informe.getNumeros()) {
            System.out.printf("Número %d: %s (%s min)%n", num.getIdNumero(), num.getNombreNumero(), num.getDuracion());
            System.out.println("  Artistas:");
            for (ArtistaDTO art : num.getArtistas()) {
                System.out.printf("    - %s (%s) [%s] %s%n",
                        art.getNombre(),
                        art.getPais(),
                        String.join(",", art.getEspecialidades()),
                        art.getApodo() != null ? "Apodo: " + art.getApodo() : "");
            }
        }
    }


    private static ArrayList<Especialidad> parsearEspecialidades(String entrada) {
        List<String> validas = Arrays.asList("ACROBACIA", "HUMOR", "MAGIA", "EQUILIBRISMO", "MALABARISMO");
        String[] tokens = entrada.split(",");
        ArrayList<Especialidad> lista = new ArrayList<>();
        for (String t : tokens) {
            String esp = t.trim().toUpperCase();
            if (esp.isEmpty()) continue;
            if (!validas.contains(esp)) {
                System.out.println("Especialidad inválida: " + esp);
                return null;
            }
            lista.add(Especialidad.valueOf(esp));
        }
        return lista;
    }

    public static String seleccionarNacionalidad() {
        Map<String, String> nacionalidades = ServicioPersona.cargarNacionalidades();

        while (true) {
            System.out.println("Selecciona una nacionalidad (introduce el código):");
            for (Map.Entry<String, String> entry : nacionalidades.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }

            String opcion = sc.nextLine().trim();
            if (nacionalidades.containsKey(opcion)) {
                return nacionalidades.get(opcion);
            }
            System.out.println("Opción inválida, inténtalo de nuevo.");
        }
    }

    private static void verFichaArtistaVista(ServicioArtista servicio, Sesion sesion) {
        Long idPersona = sesion.getCredenciales().getId();
        FichaArtistaDTO ficha = servicio.verFichaArtistaPorPersona(idPersona);

        System.out.println("\n=== FICHA DEL ARTISTA ===");
        PersonaDTO p = ficha.getPersona();
        ArtistaDTO a = ficha.getArtista();

        System.out.println("Nombre completo: " + p.getNombre() + " " + p.getApellido1() + " " + p.getApellido2());
        System.out.println("Email: " + p.getEmail());
        System.out.println("Nacionalidad: " + p.getPais());
        System.out.println("Apodo: " + (a.getApodo() != null ? a.getApodo() : "-"));
        System.out.println("Especialidades: " + (a.getEspecialidades() != null ? String.join(", ", a.getEspecialidades()) : "-"));

        System.out.println("\nTrayectoria en el circo:");
        if (ficha.getTrayectoria() == null || ficha.getTrayectoria().isEmpty()) {
            System.out.println(" - No ha participado en ningún espectáculo todavía.");
        } else {
            for (String linea : ficha.getTrayectoria()) {
                System.out.println(" - " + linea);
            }
        }
    }

    public static void gestionarEspectaculos(Sesion sesion) {
        String opcion = "";
        do {
            System.out.println("=== Gestión de espectáculos ===");
            System.out.println("1 - Crear espectáculo");
            System.out.println("2 - Modificar espectáculo");
            System.out.println("3 - Salir");
            opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                    crearEspectaculoVista(sesion);
                    break;
                case "2":
                    modificarEspectaculoVista(sesion);
                    break;
                case "3":
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (!opcion.equals("3"));
    }

    private static void crearEspectaculoVista(Sesion sesion) {
        ServicioEspectaculo servicio = new ServicioEspectaculo();

        System.out.print("Nombre del espectáculo (<=25, único): ");
        String nombre = sc.nextLine().trim();

        LocalDate ini = Entradas.leerFecha("Fecha inicio (YYYY-MM-DD): ");
        LocalDate fin = Entradas.leerFecha("Fecha fin (YYYY-MM-DD): ");

        EspectaculoDTO dto = new EspectaculoDTO();
        dto.setNombreEsp(nombre);
        dto.setFechaIni(ini);
        dto.setFechaFin(fin);

        if (sesion.getPerfil() == Perfiles.ADMIN) {
            Long idCoord = seleccionarCoordinador();
            dto.setIdCoord(idCoord);
        }

        boolean creado = servicio.crearEspectaculo(sesion, dto);
        if (!creado) {
            System.out.println("Error al crear espectáculo.");
            return;
        }
        System.out.println("Espectáculo creado. Añadamos números (mínimo 3).");
        gestionarNumerosParaEspectaculo(servicio, dto);
    }


    private static Long seleccionarCoordinador() {
        CoordinacionDAO coordDAO = new CoordinacionDAO();
        List<CoordinacionDTO> coords = coordDAO.obtenerTodosCoordinadoresDTO();
        if (coords.isEmpty()) {
            System.out.println("No hay coordinadores disponibles.");
            return null;
        }
        for (int i = 0; i < coords.size(); i++) {
            System.out.printf("%d - %s (%s)%n", i + 1, coords.get(i).getNombre(), coords.get(i).getEmail());
        }
        int sel = -1;
        do {
            System.out.print("Selecciona coordinador: ");
            try {
                sel = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                sel = -1;
            }
        } while (sel < 1 || sel > coords.size());
        return coords.get(sel - 1).getIdCoord();
    }

    private static void gestionarNumerosParaEspectaculo(ServicioEspectaculo servicio, EspectaculoDTO esp) {
        int creados = 0;
        while (true) {
            System.out.println("\nCrear número para: " + esp.getNombreEsp());
            NumeroDTO num = new NumeroDTO();

            System.out.print("Nombre del número: ");
            num.setNombreNumero(sc.nextLine().trim());

            System.out.print("Duración (x,y con y=0 ó 5): ");
            try {
                double duracion = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
                num.setDuracion(duracion);
            } catch (NumberFormatException e) {
                System.out.println("Duración inválida. Usa formato x,y con y=0 ó 5.");
                continue;
            }

            System.out.print("Orden (entero >=1): ");
            try {
                num.setOrden(Integer.parseInt(sc.nextLine().trim()));
            } catch (NumberFormatException e) {
                System.out.println("Orden inválido.");
                continue;
            }

            boolean creado = servicio.crearNumero(esp.getIdEspectaculo(), num);
            if (!creado) {
                System.out.println("No se pudo crear el número.");
                continue;
            }

            asignarArtistasVista(servicio, num);

            creados++;
            if (creados >= 3) {
                System.out.print("¿Añadir otro número? (s/n): ");
                String r = sc.nextLine().trim().toLowerCase();
                if (!r.equals("s")) break;
            }
        }
    }

    private static void asignarArtistasVista(ServicioEspectaculo servicio, NumeroDTO num) {
        ArtistaDAO artistaDAO = new ArtistaDAO();
        List<ArtistaDTO> artistas = artistaDAO.obtenerTodos();

        if (artistas.isEmpty()) {
            System.out.println("No hay artistas registrados.");
            return;
        }

        System.out.println("Selecciona artistas (separados por coma):");
        for (int i = 0; i < artistas.size(); i++) {
            ArtistaDTO a = artistas.get(i);
            System.out.printf("%d - %s (%s) [%s]%n", i + 1, a.getNombre(), a.getPais(), String.join(",", a.getEspecialidades()));
        }

        System.out.print("Índices: ");
        String input = sc.nextLine().trim();
        String[] tokens = input.split(",");
        List<Long> idsSeleccionados = new ArrayList<>();

        for (String t : tokens) {
            try {
                int idx = Integer.parseInt(t.trim());
                if (idx >= 1 && idx <= artistas.size()) {
                    idsSeleccionados.add(artistas.get(idx - 1).getIdArtista());
                }
            } catch (NumberFormatException ignored) {
            }
        }

        boolean asignados = servicio.asignarArtistasANumero(num.getIdNumero(), idsSeleccionados);
        if (!asignados) {
            System.out.println("No se pudieron asignar los artistas.");
        }
    }

    private static void modificarEspectaculoVista(Sesion sesion) {
        ServicioEspectaculo servicio = new ServicioEspectaculo();
        EspectaculoDAO espectaculoDAO = new EspectaculoDAO();

        List<EspectaculoDTO> espectaculos = espectaculoDAO.obtenerTodos();
        if (espectaculos.isEmpty()) {
            System.out.println("No hay espectáculos registrados.");
            return;
        }

        System.out.println("=== Lista de espectáculos ===");
        for (int i = 0; i < espectaculos.size(); i++) {
            EspectaculoDTO e = espectaculos.get(i);
            System.out.printf("%d - %s (%s a %s)%n", i + 1, e.getNombreEsp(), e.getFechaIni(), e.getFechaFin());
        }

        int sel = -1;
        do {
            System.out.print("Selecciona espectáculo a modificar: ");
            try {
                sel = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                sel = -1;
            }
        } while (sel < 1 || sel > espectaculos.size());

        EspectaculoDTO seleccionado = espectaculos.get(sel - 1);

        System.out.println("Introduce los nuevos datos (deja vacío para mantener el actual):");

        System.out.print("Nuevo nombre (actual: " + seleccionado.getNombreEsp() + "): ");
        String nombre = sc.nextLine().trim();
        if (!nombre.isEmpty()) seleccionado.setNombreEsp(nombre);

        System.out.print("Nueva fecha inicio (YYYY-MM-DD, actual: " + seleccionado.getFechaIni() + "): ");
        String iniStr = sc.nextLine().trim();
        if (!iniStr.isEmpty()) {
            try {
                seleccionado.setFechaIni(LocalDate.parse(iniStr));
            } catch (Exception e) {
                System.out.println("Formato inválido, se mantiene la fecha actual.");
            }
        }

        System.out.print("Nueva fecha fin (YYYY-MM-DD, actual: " + seleccionado.getFechaFin() + "): ");
        String finStr = sc.nextLine().trim();
        if (!finStr.isEmpty()) {
            try {
                seleccionado.setFechaFin(LocalDate.parse(finStr));
            } catch (Exception e) {
                System.out.println("Formato inválido, se mantiene la fecha actual.");
            }
        }

        if (sesion.getPerfil() == Perfiles.ADMIN) {
            System.out.print("¿Quieres cambiar el coordinador? (s/n): ");
            String r = sc.nextLine().trim().toLowerCase();
            if (r.equals("s")) {
                Long idCoord = seleccionarCoordinador();
                if (idCoord != null) seleccionado.setIdCoord(idCoord);
            }
        }

        boolean modificado = servicio.modificarEspectaculo(seleccionado);
        if (modificado) {
            System.out.println("Espectáculo modificado correctamente.");
        } else {
            System.out.println("No se pudo modificar el espectáculo.");
        }
    }
}
