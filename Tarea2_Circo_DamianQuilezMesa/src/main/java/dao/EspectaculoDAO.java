/**
* Clase EspectaculoDAO.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bbdd.ConexionBBDD;
import modelo.Persona;

import modelo.Espectaculo;

public class EspectaculoDAO {

	static DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("dd/MM/yyyy");

	public List<Espectaculo> obtenerEspectaculosBasico() {
	    List<Espectaculo> lista = new ArrayList<>();

	    try (Connection con = ConexionBBDD.getInstance().getConnection();
	         PreparedStatement ps = con.prepareStatement("SELECT idEspectaculo, nombreEsp, fechaini, fechafin, idCoord FROM Espectaculo");
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            Espectaculo e = new Espectaculo(
	                rs.getLong("idEspectaculo"),
	                rs.getString("nombreEsp"),
	                rs.getDate("fechaini").toLocalDate(),
	                rs.getDate("fechafin").toLocalDate(),
	                rs.getLong("idCoord")
	            );
	            lista.add(e);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // o loguear el error
	    }

	    return lista;
	}


//	String nombreUsuario = "Ramon";
//	String email = "loquesea"
//	
//	ComprobarUsuario(nombreUsusario, email);
//	
//	ComprobarUssuario(String nombreUsusarioParam, String emailParam){
//		select * from ususarios where nombreusuario like nombreUsusarioParam or email like emailParam
//				
//	}
}
