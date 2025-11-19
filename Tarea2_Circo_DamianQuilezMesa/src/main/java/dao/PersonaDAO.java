/**
* Clase PersonaDAO.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.mysql.cj.xdevapi.Statement;

import bbdd.ConexionBBDD;
import modelo.Credenciales;
import modelo.Perfiles;
import modelo.Persona;

public class PersonaDAO {


	    public boolean existeEmail(String email) {
	        String sql = "SELECT COUNT(*) FROM Persona WHERE email = ?";
	        try (Connection con = ConexionBBDD.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, email);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) return rs.getInt(1) > 0;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    public boolean existeUsuario(String usuario) {
	        String sql = "SELECT COUNT(*) FROM Persona WHERE nombreusuario = ?";
	        try (Connection con = ConexionBBDD.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, usuario);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) return rs.getInt(1) > 0;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    public long insertarPersona(Persona persona,Credenciales credenciales) {
	        String sql = "INSERT INTO Persona(nombre, apellido1, apellido2, email, pais, nombreusuario, contrasena, perfil) "
	                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	        try (Connection con = ConexionBBDD.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	        	String[] partes = persona.getNombre().split(" ");
	        	ps.setString(1, partes[0]); // nombre
	        	ps.setString(2, partes.length > 1 ? partes[1] : "");
	        	ps.setString(3, partes.length > 2 ? partes[2] : "");
	            ps.setString(4, persona.getEmail());
	            ps.setString(5, persona.getNacionalidad());
	            ps.setString(6, credenciales.getNombreUsuario());
	            ps.setString(7, credenciales.getPassword());
	            ps.setString(8, credenciales.getPerfil().toString());


	            ps.executeUpdate();

	            try (ResultSet rs = ps.getGeneratedKeys()) {
	                if (rs.next()) {
	                    return rs.getLong(1);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return -1;
	    }
	

	
}
