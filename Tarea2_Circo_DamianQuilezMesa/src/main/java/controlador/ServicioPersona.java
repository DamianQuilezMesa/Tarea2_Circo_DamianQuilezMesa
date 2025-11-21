/**
 * Clase ServicioPersona.java
 *
 * @author Damián
 * @version 2.0
 */
package controlador;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dao.ArtistaDAO;
import dao.CoordinacionDAO;
import dao.PersonaDAO;
import dto.PersonaDTO;
import modelo.Artista;
import modelo.Coordinacion;
import modelo.Perfiles;

public class ServicioPersona {

	private static final File ficheroPaises = new File(
			"src/main/resources/paises.xml");

	private final PersonaDAO personaDAO;
	private final CoordinacionDAO coordinacionDAO;
	private final ArtistaDAO artistaDAO;

	public ServicioPersona() {
		this.personaDAO = new PersonaDAO();
		this.coordinacionDAO = new CoordinacionDAO();
		this.artistaDAO = new ArtistaDAO();
	}

	/**
	 * Comprueba si el email ya existe en la BD.
	 */
	private boolean emailYaRegistrado(String email) {
		return personaDAO.emailExists(email);
	}

	/**
	 * Comprueba si el nombre de usuario ya existe en la BD.
	 */
	private boolean usuarioYaRegistrado(String usuario) {
		return personaDAO.usuarioExists(usuario);
	}

	/**
	 * Valida que email y usuario sean únicos en la BD.
	 */
	public boolean validarUnicidad(PersonaDTO persona) {
		if (emailYaRegistrado(persona.getEmail())) {
			System.out.println("Error: el email ya está registrado.");
			return false;
		}
		if (usuarioYaRegistrado(persona.getNomUsuario())) {
			System.out
					.println("Error: el nombre de usuario ya está registrado.");
			return false;
		}
		return true;
	}

	/**
	 * Registra una nueva persona en el sistema, incluyendo datos específicos
	 * según su perfil (Coordinación o Artista).
	 */
	public boolean registrarPersona(PersonaDTO persona, Artista artista,
			Coordinacion coord) {
		// Validar unicidad antes de insertar
		if (!validarUnicidad(persona))
			return false;

		// Insertar persona y recuperar id generado
		long idPersona = personaDAO.insertPersona(persona);
		if (idPersona == -1)
			return false;

		// Según perfil, insertar datos específicos
		if (persona.getPerfil() == Perfiles.ARTISTA && artista != null) {
			return artistaDAO.insertarArtista(idPersona, artista);
		} else if (persona.getPerfil() == Perfiles.COORDINACION
				&& coord != null) {
			return coordinacionDAO.insertarCoordinacion(idPersona, coord);
		}

		// Si es otro perfil, solo se inserta Persona
		return true;
	}

	/**
	 * Actualiza los datos de una persona y su información específica según el
	 * perfil (Artista o Coordinación).
	 */
	public boolean actualizarPersona(PersonaDTO persona, Artista artista,
			Coordinacion coord) {
		boolean actualizado = personaDAO.actualizarDatosPersonales(
				persona.getId(), persona.getNombre(), persona.getEmail(),
				persona.getPais());

		if (!actualizado)
			return false;

		if (persona.getPerfil() == Perfiles.ARTISTA && artista != null) {
			return artistaDAO.actualizarArtista(persona.getId(),
					artista.getApodo(), artista.getEspecialidades());
		} else if (persona.getPerfil() == Perfiles.COORDINACION
				&& coord != null) {
			return coordinacionDAO.actualizarCoordinacion(persona.getId(),
					coord.getSenior(), coord.getFechasenior());
		}

		return true;
	}

	/**
	 * Carga las nacionalidades desde el XML de países.
	 * 
	 * @return mapa con código -> nombre de país
	 */
	public static Map<String, String> cargarNacionalidades() {
		Map<String, String> paises = new LinkedHashMap<>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(ficheroPaises);
			doc.getDocumentElement().normalize();

			NodeList lista = doc.getElementsByTagName("pais");
			for (int i = 0; i < lista.getLength(); i++) {
				Node nodo = lista.item(i);
				if (nodo.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) nodo;
					String id = e.getElementsByTagName("id").item(0)
							.getTextContent().trim().toUpperCase();
					String nombre = e.getElementsByTagName("nombre").item(0)
							.getTextContent().trim();
					paises.put(id, nombre);
				}
			}
		} catch (Exception e) {
			System.out
					.println("Error leyendo XML de países: " + e.getMessage());
		}
		return paises;
	}
}
