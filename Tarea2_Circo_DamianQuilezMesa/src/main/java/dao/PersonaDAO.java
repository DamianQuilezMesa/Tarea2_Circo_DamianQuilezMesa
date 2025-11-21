/**
 * Clase PersonaDAO.java
 *
 * @author Damián
 * @version 2.0
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bbdd.ConexionBBDD;
import dto.PersonaDTO;
import modelo.Perfiles;

public class PersonaDAO {

	/**
	 * Busca el idPersona asociado a un nombre de usuario.
	 */
	public long findIdByUsuario(String nombreUsuario) {
		String sql = "SELECT idPersona FROM Persona WHERE nombreUsuario = ?";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, nombreUsuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("idPersona");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Obtiene una persona por su nombre de usuario.
	 */
	public PersonaDTO obtenerPorUsuario(String usuario) {
		String sql = "SELECT idPersona, nombre, apellido1, apellido2, email, pais, nombreUsuario, perfil "
				+ "FROM Persona WHERE nombreUsuario = ?";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, usuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new PersonaDTO(rs.getLong("idPersona"),
							rs.getString("nombre"), rs.getString("apellido1"),
							rs.getString("apellido2"), rs.getString("email"),
							rs.getString("pais"), rs.getString("nombreUsuario"),
							null, // contrasena no se expone aquí
							Perfiles.valueOf(
									rs.getString("perfil").toUpperCase()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Comprueba si un email ya existe en la tabla Persona.
	 * 
	 */
	public boolean emailExists(String email) {
		String sql = "SELECT COUNT(*) FROM Persona WHERE email = ?";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Comprueba si un nombre de usuario ya existe en la tabla Persona.
	 * 
	 */
	public boolean usuarioExists(String usuario) {
		String sql = "SELECT COUNT(*) FROM Persona WHERE nombreUsuario = ?";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, usuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * Actualiza los datos personales de una persona.
	 */
	public boolean actualizarDatosPersonales(long idPersona, String nombre,
			String email, String pais) {
		String sql = "UPDATE Persona SET nombre=?, email=?, pais=? WHERE idPersona=?";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, nombre);
			ps.setString(2, email);
			ps.setString(3, pais);
			ps.setLong(4, idPersona);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * Inserta una nueva persona en la BD.
	 * 
	 */
	public long insertPersona(PersonaDTO persona) {
		String sql = "INSERT INTO Persona(nombre, apellido1, apellido2, email, pais, nombreUsuario, contrasena, perfil) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, persona.getNombre());
			ps.setString(2, persona.getApellido1());
			ps.setString(3, persona.getApellido2());
			ps.setString(4, persona.getEmail());
			ps.setString(5, persona.getPais());
			ps.setString(6, persona.getNomUsuario());
			ps.setString(7, persona.getContrasena());
			ps.setString(8, persona.getPerfil().name());

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
