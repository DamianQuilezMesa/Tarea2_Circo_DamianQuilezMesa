/**
 * Clase NumeroDAO.java
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
import java.util.ArrayList;
import java.util.List;

import bbdd.ConexionBBDD;
import dto.NumeroDTO;
import dto.ArtistaDTO;

public class NumeroDAO {


    /**
     * Obtiene todos los números de un espectáculo concreto.
     * Incluye los artistas asociados a cada número.
     */
    public List<NumeroDTO> obtenerNumerosPorEspectaculo(long idEspectaculo) {
        List<NumeroDTO> lista = new ArrayList<>();
        String sql = "SELECT idNumero, nombreNumero, duracion, orden " +
                     "FROM Numero WHERE idEspectaculo = ? ORDER BY orden";

        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idEspectaculo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NumeroDTO numero = new NumeroDTO(
                            rs.getLong("idNumero"),
                            rs.getString("nombreNumero"),
                            rs.getDouble("duracion"),
                            rs.getInt("orden"),
                            new ArrayList<>() // artistas se cargan aparte
                    );

                    // cargar artistas del número
                    ArtistaDAO artistaDAO = new ArtistaDAO();
                    List<ArtistaDTO> artistas = artistaDAO.obtenerArtistasPorNumero(numero.getIdNumero());
                    numero.setArtistas(artistas);

                    lista.add(numero);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Inserta un número vinculado a un espectáculo y devuelve el id generado.
     *
     */
    public Long insertar(Long idEspectaculo, NumeroDTO num) {
        String sql = "INSERT INTO Numero (nombreNumero, duracion, orden, idEspectaculo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, num.getNombreNumero());
            ps.setDouble(2, num.getDuracion());
            ps.setInt(3, num.getOrden());
            ps.setLong(4, idEspectaculo);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        Long idGenerado = rs.getLong(1);
                        num.setIdNumero(idGenerado); // guardar en el DTO
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
     * Actualiza los datos de un número.
     *
     */
    public boolean actualizar(NumeroDTO num) {
        String sql = "UPDATE Numero SET nombreNumero=?, duracion=?, orden=? WHERE idNumero=?";
        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, num.getNombreNumero());
            ps.setDouble(2, num.getDuracion());
            ps.setInt(3, num.getOrden());
            ps.setLong(4, num.getIdNumero());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
