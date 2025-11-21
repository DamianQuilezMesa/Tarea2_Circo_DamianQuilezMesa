/**
 * Clase ServicioCredenciales.java
 
 * @author Damián
 * @version 1.0
 */
package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import dao.CredencialesDAO;
import modelo.Credenciales;
import modelo.Perfiles;
import modelo.Sesion;

public class ServicioCredenciales {

    private final CredencialesDAO credencialesDAO = new CredencialesDAO();
    private String usuarioAdmin;
    private String passwordAdmin;

    public ServicioCredenciales() {
        cargarCredencialesAdmin();
    }

    /**
     * Carga las credenciales del administrador desde application.properties.
     * Si no se encuentran, usuarioAdmin y passwordAdmin quedarán en null.
     */
    private void cargarCredencialesAdmin() {
        Properties prop = new Properties();
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is != null) {
                prop.load(is);
                usuarioAdmin = prop.getProperty("usuarioAdmin");
                passwordAdmin = prop.getProperty("passwordAdmin");
            } else {
                System.out.println("No se encontró application.properties");
            }
        } catch (IOException e) {
            System.out.println("Error cargando credenciales de administrador.");
            e.printStackTrace();
        }
    }

    /**
     * Comprueba si las credenciales introducidas corresponden al administrador.
     */
    private boolean esAdmin(String usuario, String password) {
        return usuarioAdmin != null && passwordAdmin != null
                && usuario.equals(usuarioAdmin)
                && password.equals(passwordAdmin);
    }

    /**
     * Valida las credenciales de un usuario normal contra la BBDD.
     */
    private Credenciales validarCredencialesUsuario(String usuario, String password) {
        return credencialesDAO.obtenerCredenciales().stream()
                .filter(c -> c.getNomUsuario().equalsIgnoreCase(usuario)
                        && c.getContrasena().equals(password))
                .findFirst()
                .orElse(null);
    }

    /**
     * Realiza el login de un usuario o administrador.
     */
    public boolean login(Sesion sesion, String usuario, String password) {
        if (usuario == null || password == null || usuario.isBlank() || password.isBlank()) {
            return false; // validación básica de entrada
        }

        // Login administrador
        if (esAdmin(usuario, password)) {
            sesion.iniciarSesionAdmin(usuarioAdmin, Perfiles.ADMIN);
            return true;
        }

        // Login usuario normal
        Credenciales cred = validarCredencialesUsuario(usuario, password);
        if (cred != null) {
            sesion.iniciarSesionPerfiles(cred);
            return true;
        }

        return false;
    }
}
