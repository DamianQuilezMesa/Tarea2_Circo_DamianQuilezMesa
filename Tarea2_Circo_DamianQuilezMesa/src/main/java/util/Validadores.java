/**
* Clase Validadores.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package util;

public class Validadores {

	    // Valida nombres y apellidos (solo letras y espacios, mínimo 2 caracteres)
		public static boolean esNombreValido(String input) {
	    return input != null && input.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ]{2,}$");
	}

	    // Valida emails con formato bloque@bloque.bloque
	    public static boolean esEmailValido(String input) {
	        return input != null && input.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	    }

	    // Valida nombre de usuario (mínimo 3 caracteres, sin espacios)
	    public static boolean esUsuarioValido(String usuario) {
	        return usuario != null && usuario.length() > 2 && !usuario.contains(" ");
	    }

	    // Valida contraseña (mínimo 3 caracteres, sin espacios)
	    public static boolean esContrasenaValida(String contrasena) {
	        return contrasena != null && contrasena.length() > 2 && !contrasena.contains(" ");
	    }
	}

	

