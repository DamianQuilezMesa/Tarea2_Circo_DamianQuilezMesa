/**
* Clase ServicioCredenciales.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import dao.CredencialesDAO;
import modelo.Credenciales;
import modelo.Perfiles;
import modelo.Sesion;

public class ServicioCredenciales {

	private CredencialesDAO credencialesDAO = new CredencialesDAO();
	private String usuarioAdmin;
	private String passwordAdmin;

	public ServicioCredenciales() {
		Properties prop = new Properties();
		try (InputStream is = getClass().getClassLoader()
				.getResourceAsStream("application.properties")) {
			prop.load(is);
			usuarioAdmin = prop.getProperty("usuarioAdmin");
			passwordAdmin = prop.getProperty("passwordAdmin");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean servicioLogin(Sesion sesion, String usuario, String password) {
		if (usuario.isBlank() || password.isBlank())
			return false;

		if (usuario.equals(usuarioAdmin) && password.equals(passwordAdmin)) {
			sesion.iniciarSesionAdmin(usuarioAdmin, Perfiles.ADMIN);;
			return true;
		}

		for (Credenciales c : credencialesDAO.obtenerCredenciales()) {
			if (c.getNombreUsuario().equalsIgnoreCase(usuario)
					&& c.getPassword().equals(password)) {
				sesion.iniciarSesionPerfiles(c);
				return true;
			}
		}

		return false;
	}

}
