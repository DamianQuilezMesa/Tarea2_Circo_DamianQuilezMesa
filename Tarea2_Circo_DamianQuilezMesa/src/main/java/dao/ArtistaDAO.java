/**
 * @author Damián
 * @version 2.0
 */

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import bbdd.ConexionBBDD;
import dto.ArtistaDTO;
import dto.FichaArtistaDTO;
import dto.PersonaDTO;
import modelo.Artista;
import modelo.Especialidad;

public class ArtistaDAO {

	/**
	 * Inserta un nuevo artista en la base de datos. Usado en casos de alta.
	 */
	public boolean insertarArtista(Long idPersona, Artista artista) {
		String sql = "INSERT INTO Artista (idPersona, apodo, especialidades) VALUES (?, ?, ?)";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, idPersona);
			ps.setString(2, artista.getApodo());

			// Guardamos especialidades como string separado por comas
			String especialidadesStr = artista.getEspecialidades().stream()
					.map(Enum::name).collect(Collectors.joining(","));
			ps.setString(3, especialidadesStr);

			ps.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertarArtistas(Long idNumero, List<Long> idsArtistas) {
		String sql = "INSERT INTO Numero_Artista (idNumero, idArtista) VALUES (?, ?)";
		try (Connection conn = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			for (Long idArtista : idsArtistas) {
				ps.setLong(1, idNumero);
				ps.setLong(2, idArtista);
				ps.addBatch();
			}
			ps.executeBatch();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Obtiene todos los artistas que participan en un número concreto. Este
	 * método es el que se usa en CU4 (Informe de espectáculo).
	 */
	public List<ArtistaDTO> obtenerArtistasPorNumero(long idNumero) {
		String sql = "SELECT p.nombre, p.pais, a.apodo, a.especialidades "
				+ "FROM Numero_Artista na "
				+ "JOIN Artista a ON na.idArt = a.idArt "
				+ "JOIN Persona p ON a.idPersona = p.idPersona "
				+ "WHERE na.idNumero = ?";
		List<ArtistaDTO> lista = new ArrayList<>();

		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, idNumero);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ArtistaDTO a = new ArtistaDTO();
					a.setNombre(rs.getString("nombre"));
					a.setPais(rs.getString("pais"));
					a.setApodo(rs.getString("apodo"));

					String espStr = rs.getString("especialidades");
					if (espStr != null && !espStr.isEmpty()) {
						a.setEspecialidades(new ArrayList<>(
								Arrays.asList(espStr.split(","))));
					} else {
						a.setEspecialidades(new ArrayList<>());
					}

					lista.add(a);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

	/**
	 * Obtiene un artista por su idPersona. Usado en otros CU (no en CU4).
	 */
	public Artista obtenerPorIdPersona(long idPersona) {
		String sql = "SELECT apodo, especialidades FROM Artista WHERE idPersona = ?";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, idPersona);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Artista a = new Artista();
					a.setApodo(rs.getString("apodo"));

					String espStr = rs.getString("especialidades");
					if (espStr != null && !espStr.isEmpty()) {
						ArrayList<Especialidad> lista = new ArrayList<>();
						for (String s : espStr.split(",")) {
							lista.add(Especialidad
									.valueOf(s.trim().toUpperCase()));
						}
						a.setEspecialidades(lista);
					}
					return a;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean actualizarArtista(long idPersona, String apodo,
			ArrayList<Especialidad> especialidades) {
		String sql = "UPDATE Artista SET apodo=?, especialidades=? WHERE idPersona=?";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, apodo);

			String especialidadesStr = especialidades.stream().map(Enum::name)
					.collect(Collectors.joining(","));
			ps.setString(2, especialidadesStr);

			ps.setLong(3, idPersona);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<ArtistaDTO> obtenerTodos() {
		List<ArtistaDTO> lista = new ArrayList<>();
		String sql = "SELECT idArt, apodo, especialidades, p.nombre, p.pais "
				+ "FROM Artista a JOIN Persona p ON a.idPersona = p.idPersona";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				ArtistaDTO dto = new ArtistaDTO();
				dto.setIdArtista(rs.getLong("idArt"));
				dto.setApodo(rs.getString("apodo"));
				dto.setNombre(rs.getString("nombre"));
				dto.setPais(rs.getString("pais"));

				String espStr = rs.getString("especialidades");
				if (espStr != null && !espStr.isEmpty()) {
					dto.setEspecialidades(
							new ArrayList<>(Arrays.asList(espStr.split(","))));
				} else {
					dto.setEspecialidades(new ArrayList<>());
				}
				lista.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

	public List<Long> obtenerTodosIds() {
		List<Long> ids = new ArrayList<>();
		String sql = "SELECT idArt FROM Artista";
		try (Connection con = ConexionBBDD.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				ids.add(rs.getLong("idArt"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ids;
	}

	public boolean reasignarArtistasANumero(long idNumero,
			List<Long> idsArtistas) {
		String deleteSql = "DELETE FROM Numero_Artista WHERE idNumero=?";
		String insertSql = "INSERT INTO Numero_Artista (idNumero, idArt) VALUES (?, ?)";
		try (Connection conn = ConexionBBDD.getInstance().getConnection();
				PreparedStatement psDelete = conn.prepareStatement(deleteSql)) {

			// 1. Borrar asignaciones anteriores
			psDelete.setLong(1, idNumero);
			psDelete.executeUpdate();

			// 2. Insertar nuevas asignaciones
			try (PreparedStatement psInsert = conn
					.prepareStatement(insertSql)) {
				for (Long idArtista : idsArtistas) {
					psInsert.setLong(1, idNumero);
					psInsert.setLong(2, idArtista);
					psInsert.addBatch();
				}
				psInsert.executeBatch();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Long obtenerIdArtistaPorPersona(Long idPersona) {
	    String sql = "SELECT idArt FROM Artista WHERE idPersona=?";
	    try (Connection con = ConexionBBDD.getInstance().getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setLong(1, idPersona);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getLong("idArt");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}




	public FichaArtistaDTO obtenerFicha(long idArtista) {
	    FichaArtistaDTO ficha = new FichaArtistaDTO();
	    PersonaDTO persona = new PersonaDTO();
	    ArtistaDTO artista = new ArtistaDTO();

	    // 1. Datos personales + profesionales
	    String sqlDatos = "SELECT p.idPersona, p.nombre, p.apellido1, p.apellido2, p.email, p.pais, " +
	                      "a.idArt, a.apodo, a.especialidades " +
	                      "FROM Artista a " +
	                      "JOIN Persona p ON a.idPersona = p.idPersona " +
	                      "WHERE a.idArt = ?";
	    try (Connection con = ConexionBBDD.getInstance().getConnection();
	         PreparedStatement ps = con.prepareStatement(sqlDatos)) {
	        ps.setLong(1, idArtista);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                // Persona
	                persona.setId(rs.getLong("idPersona"));
	                persona.setNombre(rs.getString("nombre"));
	                persona.setApellido1(rs.getString("apellido1"));
	                persona.setApellido2(rs.getString("apellido2"));
	                persona.setEmail(rs.getString("email"));
	                persona.setPais(rs.getString("pais"));

	                // Artista
	                artista.setIdArtista(rs.getLong("idArt"));
	                artista.setApodo(rs.getString("apodo"));
	                String espStr = rs.getString("especialidades");
	                artista.setEspecialidades(espStr != null && !espStr.isEmpty()
	                        ? new ArrayList<>(Arrays.asList(espStr.split(",")))
	                        : new ArrayList<>());
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    // 2. Trayectoria: espectáculos y números
	    String sqlTrayectoria = "SELECT e.idEspectaculo, e.nombreEsp, n.idNumero, n.nombreNumero " +
	                            "FROM Numero_Artista na " +
	                            "JOIN Numero n ON na.idNumero = n.idNumero " +
	                            "JOIN Espectaculo e ON n.idEspectaculo = e.idEspectaculo " +
	                            "WHERE na.idArt = ? " +
	                            "ORDER BY e.nombreEsp, n.orden";
	    List<String> trayectoria = new ArrayList<>();
	    try (Connection con = ConexionBBDD.getInstance().getConnection();
	         PreparedStatement ps = con.prepareStatement(sqlTrayectoria)) {
	        ps.setLong(1, idArtista);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                trayectoria.add("Espectáculo [" + rs.getLong("idEspectaculo") + "] " +
	                                rs.getString("nombreEsp") +
	                                " → Número [" + rs.getLong("idNumero") + "] " +
	                                rs.getString("nombreNumero"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    // 3. Montar DTO compuesto
	    ficha.setPersona(persona);
	    ficha.setArtista(artista);
	    ficha.setTrayectoria(trayectoria);

	    return ficha;
	}


}
