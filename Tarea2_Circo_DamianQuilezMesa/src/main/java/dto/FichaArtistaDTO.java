/**
* Clase FichaArtistaDTO.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package dto;

import java.util.List;

public class FichaArtistaDTO {
	private PersonaDTO persona; // datos personales
	private ArtistaDTO artista; // datos profesionales
	private List<String> trayectoria; // espectáculos y números

	public PersonaDTO getPersona() {
		return persona;
	}

	public void setPersona(PersonaDTO persona) {
		this.persona = persona;
	}

	public ArtistaDTO getArtista() {
		return artista;
	}

	public void setArtista(ArtistaDTO artista) {
		this.artista = artista;
	}

	public List<String> getTrayectoria() {
		return trayectoria;
	}

	public void setTrayectoria(List<String> trayectoria) {
		this.trayectoria = trayectoria;
	}

	@Override
	public String toString() {
		return "FichaArtistaDTO [persona=" + persona + ", artista=" + artista
				+ ", trayectoria=" + trayectoria + "]";
	}
}
