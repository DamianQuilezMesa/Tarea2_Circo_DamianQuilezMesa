/**
 * Clase CredencialesDAO.java
 * @author Damián
 * @version 2.0
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

    /**
     * Recupera todas las credenciales de la BD.
     */
    public Set<Credenciales> obtenerCredenciales() {
        Set<Credenciales> lista = new HashSet<>();
        String sql = "SELECT idPersona, nombreUsuario, contrasena, perfil FROM Persona";

        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToCredenciales(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Busca credenciales por nombre de usuario.
     */
    public Credenciales findByUsuario(String usuario) {
        String sql = "SELECT idPersona, nombreUsuario, contrasena, perfil FROM Persona WHERE nombreUsuario = ?";
        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCredenciales(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Busca credenciales por usuario y contraseña.
     * Útil para simplificar el login en ServicioCredenciales.
     */
    public Credenciales findByUsuarioYPassword(String usuario, String password) {
        String sql = "SELECT idPersona, nombreUsuario, contrasena, perfil FROM Persona WHERE nombreUsuario = ? AND contrasena = ?";
        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCredenciales(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserta nuevas credenciales en la BD (tabla Persona).
    */
    public boolean insertCredenciales(Credenciales cred) {
        String sql = "INSERT INTO Persona(nombreUsuario, contrasena, perfil) VALUES (?, ?, ?)";
        try (Connection con = ConexionBBDD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cred.getNomUsuario());
            ps.setString(2, cred.getContrasena());
            ps.setString(3, cred.getPerfil().name());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Convierte un ResultSet en un objeto Credenciales.
     */
    private Credenciales mapResultSetToCredenciales(ResultSet rs) throws SQLException {
        Credenciales c = new Credenciales();
        c.setId(rs.getLong("idPersona"));
        c.setNomUsuario(rs.getString("nombreUsuario"));
        c.setContrasena(rs.getString("contrasena"));
        c.setPerfil(Perfiles.valueOf(rs.getString("perfil").toUpperCase()));
        return c;
    }
}

