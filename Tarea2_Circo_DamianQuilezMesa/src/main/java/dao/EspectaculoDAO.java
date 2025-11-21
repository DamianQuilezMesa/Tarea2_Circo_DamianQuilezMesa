/**
 * Clase EspectaculoDAO.java
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import bbdd.ConexionBBDD;
import dto.EspectaculoDTO;

public class EspectaculoDAO {

	
    /**
     * Obtiene todos los espectáculos con sus datos básicos.
     */
    public List<EspectaculoDTO> obtenerEspectaculosBasico() {
        List<EspectaculoDTO> lista = new ArrayList<>();
        String sql = "SELECT idEspectaculo, nombreEsp, fechaIni, fechaFin, idCoord FROM Espectaculo";

        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EspectaculoDTO e = new EspectaculoDTO(
                        rs.getLong("idEspectaculo"),
                        rs.getString("nombreEsp"),
                        rs.getDate("fechaIni").toLocalDate(),
                        rs.getDate("fechaFin").toLocalDate(),
                        rs.getLong("idCoord")
                );
                lista.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // aquí podrías usar un logger
        }
        return lista;
    }

    /**
     * Obtiene un espectáculo concreto por su ID.
     */
    public EspectaculoDTO obtenerPorId(long idEspectaculo) {
        String sql = "SELECT idEspectaculo, nombreEsp, fechaIni, fechaFin, idCoord FROM Espectaculo WHERE idEspectaculo = ?";
        EspectaculoDTO espectaculo = null;

        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idEspectaculo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    espectaculo = new EspectaculoDTO(
                            rs.getLong("idEspectaculo"),
                            rs.getString("nombreEsp"),
                            rs.getDate("fechaIni").toLocalDate(),
                            rs.getDate("fechaFin").toLocalDate(),
                            rs.getLong("idCoord")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return espectaculo;
    }

    /**
     * Obtiene todos los espectáculos completos.
     */
    public List<EspectaculoDTO> obtenerTodos() {
        List<EspectaculoDTO> lista = new ArrayList<>();
        String sql = "SELECT idEspectaculo, nombreEsp, fechaIni, fechaFin, idCoord FROM Espectaculo";

        try (Connection conn = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EspectaculoDTO dto = new EspectaculoDTO();
                dto.setIdEspectaculo(rs.getLong("idEspectaculo"));
                dto.setNombreEsp(rs.getString("nombreEsp"));
                dto.setFechaIni(rs.getDate("fechaIni").toLocalDate());
                dto.setFechaFin(rs.getDate("fechaFin").toLocalDate());
                dto.setIdCoord(rs.getLong("idCoord"));
                lista.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    /**
     * Inserta un nuevo espectáculo en la BD
     */
    public Long insertar(EspectaculoDTO esp, Long idCoord) {
        String sql = "INSERT INTO Espectaculo (nombreEsp, fechaIni, fechaFin, idCoord) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, esp.getNombreEsp());
            ps.setDate(2, Date.valueOf(esp.getFechaIni()));
            ps.setDate(3, Date.valueOf(esp.getFechaFin()));
            ps.setLong(4, idCoord);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        Long idGenerado = rs.getLong(1);
                        esp.setIdEspectaculo(idGenerado);
                        return idGenerado;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza los datos de un espectáculo.
     */
    public boolean actualizar(EspectaculoDTO esp) {
        String sql = "UPDATE Espectaculo SET nombreEsp=?, fechaIni=?, fechaFin=?, idCoord=? WHERE idEspectaculo=?";
        try (Connection conn = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, esp.getNombreEsp());
            ps.setDate(2, Date.valueOf(esp.getFechaIni()));
            ps.setDate(3, Date.valueOf(esp.getFechaFin()));
            ps.setLong(4, esp.getIdCoord());
            ps.setLong(5, esp.getIdEspectaculo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Comprueba si existe un espectáculo con ese nombre.
     */
    public boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM Espectaculo WHERE nombreEsp=?";
        try (Connection conn = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
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
     * Comprueba si el nombre está en uso por otro espectáculo distinto al id dado.
     */
    public boolean existeNombreParaOtro(Long idEsp, String nombre) {
        String sql = "SELECT COUNT(*) FROM Espectaculo WHERE nombreEsp=? AND idEspectaculo<>?";
        try (Connection conn = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setLong(2, idEsp);
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
}

