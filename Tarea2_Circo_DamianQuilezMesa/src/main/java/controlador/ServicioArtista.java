/**
 * Clase ServicioArtista.java
 *
 * Servicio para gestionar la información de artistas.
 * Se apoya en ArtistaDAO para acceder a la base de datos
 * y devolver DTOs con la ficha completa del artista.
 *
 * @author Damián
 * @version 1.0
 */
package controlador;

import dao.ArtistaDAO;
import dto.FichaArtistaDTO;

public class ServicioArtista {

    private final ArtistaDAO artistaDAO = new ArtistaDAO();

    /**
     * Obtiene la ficha completa de un artista a partir de su idArtista.
     * @param idArtista identificador único del artista
     * @return FichaArtistaDTO con datos personales, profesionales y trayectoria
     */
    public FichaArtistaDTO verFichaArtista(Long idArtista) {
        if (idArtista == null) {
            return null; // seguridad ante valores nulos
        }
        return artistaDAO.obtenerFicha(idArtista);
    }

    /**
     * Obtiene la ficha completa de un artista a partir de su idPersona.
     * Internamente traduce idPersona -> idArtista y luego devuelve la ficha.
     * @param idPersona identificador único de la persona asociada al artista
     * @return FichaArtistaDTO con datos personales, profesionales y trayectoria
     */
    public FichaArtistaDTO verFichaArtistaPorPersona(Long idPersona) {
        if (idPersona == null) {
            return null;
        }
        Long idArtista = artistaDAO.obtenerIdArtistaPorPersona(idPersona);
        return verFichaArtista(idArtista);
    }
}
