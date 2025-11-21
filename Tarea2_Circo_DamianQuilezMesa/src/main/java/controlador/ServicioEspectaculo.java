/**
 * Clase ServicioEspectaculo.java
 *
 * @author Damián
 * @version 3.0
 */
package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dao.EspectaculoDAO;
import dao.NumeroDAO;
import dao.ArtistaDAO;
import dao.CoordinacionDAO;
import dto.ArtistaDTO;
import dto.EspectaculoDTO;
import dto.NumeroDTO;
import dto.CoordinacionDTO;
import dto.InformeEspectaculo;
import modelo.Perfiles;
import modelo.Sesion;

public class ServicioEspectaculo {

    private final EspectaculoDAO espectaculoDAO = new EspectaculoDAO();
    private final CoordinacionDAO coordinacionDAO = new CoordinacionDAO();
    private final NumeroDAO numeroDAO = new NumeroDAO();
    private final ArtistaDAO artistaDAO = new ArtistaDAO();

    /**
     * Lista todos los espectáculos básicos.
     */
    public List<EspectaculoDTO> obtenerTodosLosEspectaculos() {
        List<EspectaculoDTO> lista = espectaculoDAO.obtenerEspectaculosBasico();
        return lista != null ? lista : new ArrayList<>();
    }

    
    public InformeEspectaculo generarInformeEspectaculo(long idEspectaculo) {
        InformeEspectaculo informe = new InformeEspectaculo();

        EspectaculoDTO esp = espectaculoDAO.obtenerPorId(idEspectaculo);
        if (esp == null) {
            return informe; // informe vacío, la vista lo maneja
        }
        informe.setEspectaculo(esp);

        CoordinacionDTO coord = coordinacionDAO.obtenerCoordinadorPorId(esp.getIdCoord());
        informe.setCoordinador(coord);

        List<NumeroDTO> numeros = numeroDAO.obtenerNumerosPorEspectaculo(idEspectaculo);
        if (numeros == null) numeros = new ArrayList<>();

        for (NumeroDTO num : numeros) {
            List<ArtistaDTO> artistas = artistaDAO.obtenerArtistasPorNumero(num.getIdNumero());
            num.setArtistas(artistas != null ? artistas : new ArrayList<>());
        }

        informe.setNumeros(numeros);
        return informe;
    }


    private boolean validarNombreEspectaculo(String nombre) {
        return nombre != null && !nombre.isBlank() && nombre.length() <= 25;
    }

    private boolean validarRangoFechas(LocalDate ini, LocalDate fin) {
        if (ini == null || fin == null) return false;
        if (fin.isBefore(ini)) return false;
        return !ini.plusYears(1).isBefore(fin); // fin no más de 1 año después de ini
    }

    private boolean validarDuracion(double duracion) {
        if (duracion < 0) return false;
        double parteDecimal = duracion - Math.floor(duracion);
        return parteDecimal == 0.0 || parteDecimal == 0.5;
    }

    
    public boolean crearEspectaculo(Sesion sesion, EspectaculoDTO nuevo) {
        // Validaciones
        if (!validarNombreEspectaculo(nuevo.getNombreEsp())) return false;
        if (!validarRangoFechas(nuevo.getFechaIni(), nuevo.getFechaFin())) return false;
        if (espectaculoDAO.existeNombre(nuevo.getNombreEsp())) return false;

        // Coordinador según perfil
        Long idCoord;
        if (sesion.getPerfil() == Perfiles.COORDINACION) {
            idCoord = sesion.getCredenciales().getId(); // idPersona como coordinador
        } else if (sesion.getPerfil() == Perfiles.ADMIN) {
            idCoord = nuevo.getIdCoord();
            if (idCoord == null) return false;
        } else {
            return false; // perfiles no autorizados
        }

        // Persistencia
        Long idGenerado = espectaculoDAO.insertar(nuevo, idCoord);
        return idGenerado != null && idGenerado > 0;
    }

    public boolean modificarEspectaculo(EspectaculoDTO actualizado) {
        if (!validarNombreEspectaculo(actualizado.getNombreEsp())) return false;
        if (!validarRangoFechas(actualizado.getFechaIni(), actualizado.getFechaFin())) return false;

        if (espectaculoDAO.existeNombreParaOtro(actualizado.getIdEspectaculo(), actualizado.getNombreEsp()))
            return false;

        return espectaculoDAO.actualizar(actualizado);
    }


    public boolean crearNumero(long idEspectaculo, NumeroDTO nuevo) {
        if (!validarDuracion(nuevo.getDuracion())) return false;
        if (nuevo.getOrden() < 1) return false;

        List<NumeroDTO> existentes = numeroDAO.obtenerNumerosPorEspectaculo(idEspectaculo);
        boolean colision = existentes.stream().anyMatch(n -> n.getOrden() == nuevo.getOrden());
        if (colision) return false;

        Long idGenerado = numeroDAO.insertar(idEspectaculo, nuevo);
        return idGenerado != null && idGenerado > 0;
    }

    public boolean modificarNumero(NumeroDTO actualizado) {
        if (!validarDuracion(actualizado.getDuracion())) return false;
        if (actualizado.getOrden() < 1) return false;
        return numeroDAO.actualizar(actualizado);
    }


    public boolean asignarArtistasANumero(long idNumero, List<Long> idsArtistas) {
        if (idsArtistas == null) idsArtistas = Collections.emptyList();

        // Validar que todos los artistas existen
        List<Long> existentes = artistaDAO.obtenerTodosIds();
        boolean todosValidos = idsArtistas.stream().allMatch(existentes::contains);
        if (!todosValidos) return false;

        // Persistencia: reemplazo total de asignaciones
        return artistaDAO.reasignarArtistasANumero(idNumero, idsArtistas);
    }
}

