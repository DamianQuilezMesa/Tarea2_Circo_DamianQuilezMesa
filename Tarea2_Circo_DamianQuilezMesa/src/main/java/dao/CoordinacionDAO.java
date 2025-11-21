/**
 * Clase CoordinacionDAO.java
 *
 *
 * @author Damián
 * @version 3.0
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import bbdd.ConexionBBDD;
import modelo.Coordinacion;
import dto.CoordinacionDTO;

public class CoordinacionDAO {

    /**
     * Inserta un nuevo coordinador en la base de datos.
     */
    public boolean insertarCoordinacion(long idPersona, Coordinacion coord) {
        String sql = "INSERT INTO Coordinacion (idPersona, senior, fechaSenior) VALUES (?, ?, ?)";
        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idPersona);
            ps.setBoolean(2, coord.getSenior());

            if (coord.getFechasenior() != null) {
                ps.setDate(3, Date.valueOf(coord.getFechasenior()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtiene un coordinador por su idPersona.
     */
    public Coordinacion obtenerPorIdPersona(long idPersona) {
        String sql = "SELECT senior, fechaSenior FROM Coordinacion WHERE idPersona = ?";
        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idPersona);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coordinacion c = new Coordinacion();
                    c.setSenior(rs.getBoolean("senior"));
                    Date d = rs.getDate("fechaSenior");
                    c.setFechasenior(d != null ? d.toLocalDate() : null);
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene un coordinador por su idCoord y devuelve un DTO.
     * Usado en CU4 (Informe de espectáculo).
     */
    public CoordinacionDTO obtenerCoordinadorPorId(long idCoord) {
        String sql = "SELECT p.nombre, p.email, c.senior " +
                     "FROM Coordinacion c " +
                     "JOIN Persona p ON c.idPersona = p.idPersona " +
                     "WHERE c.idCoord = ?";
        CoordinacionDTO coordinador = null;

        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idCoord);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    coordinador = new CoordinacionDTO();
                    coordinador.setIdCoord(idCoord);
                    coordinador.setNombre(rs.getString("nombre"));
                    coordinador.setEmail(rs.getString("email"));
                    coordinador.setSenior(rs.getBoolean("senior"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coordinador;
    }

    /**
     * Obtiene todos los coordinadores como DTO.
     */
    public List<CoordinacionDTO> obtenerTodosCoordinadoresDTO() {
        List<CoordinacionDTO> lista = new ArrayList<>();
        String sql = "SELECT c.idCoord, c.idPersona, p.nombre, p.email, c.senior, c.fechaSenior " +
                     "FROM Coordinacion c JOIN Persona p ON c.idPersona = p.idPersona";

        try (Connection conn = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CoordinacionDTO dto = new CoordinacionDTO();
                dto.setIdCoord(rs.getLong("idCoord"));
                dto.setIdPersona(rs.getLong("idPersona"));
                dto.setNombre(rs.getString("nombre"));
                dto.setEmail(rs.getString("email"));
                dto.setSenior(rs.getBoolean("senior"));

                Date fecha = rs.getDate("fechaSenior");
                dto.setFechaSenior(fecha != null ? fecha.toLocalDate() : null);

                lista.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Actualiza los datos de un coordinador en la base de datos.
     */
    public boolean actualizarCoordinacion(long idPersona, boolean senior, LocalDate fechaSenior) {
        String sql = "UPDATE Coordinacion SET senior=?, fechaSenior=? WHERE idPersona=?";
        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, senior);

            if (fechaSenior != null) {
                ps.setDate(2, Date.valueOf(fechaSenior));
            } else {
                ps.setNull(2, Types.DATE);
            }

            ps.setLong(3, idPersona);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

