/**
* Clase CredencialesDAO.java
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

import bbdd.ConexionBBDD;
import modelo.Credenciales;
import modelo.Perfiles;

public class CredencialesDAO {

	public Set<Credenciales> obtenerCredenciales() {
		Set<Credenciales> lista = new HashSet<>();
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(
						"SELECT idPersona, nombreUsuario, contrasena, perfil FROM Persona");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Credenciales c = new Credenciales();
				c.setId(rs.getLong("idPersona"));
				c.setNombreUsuario(rs.getString("nombreUsuario"));
				c.setPassword(rs.getString("contrasena"));
				c.setPerfil(
						Perfiles.valueOf(rs.getString("perfil").toUpperCase()));
				lista.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}
}
