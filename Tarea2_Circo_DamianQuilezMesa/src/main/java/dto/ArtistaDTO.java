/**
* Clase ArtistaDTO.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package dto;

import java.util.ArrayList;

public class ArtistaDTO {
	private Long idArtista;
	private String nombre;
	private String pais;
	private String apodo;
	private ArrayList<String> especialidades; // usa genérico para seguridad de
												// tipos

	public ArtistaDTO() {
	}


	public ArtistaDTO(Long idArtista, String nombre, String pais, String apodo,
			ArrayList<String> especialidades) {
		this.idArtista = idArtista;
		this.nombre = nombre;
		this.pais = pais;
		this.apodo = apodo;
		this.especialidades = especialidades;
	}

	public Long getIdArtista() {
		return idArtista;
	}

	public void setIdArtista(Long idArtista) {
		this.idArtista = idArtista;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getApodo() {
		return apodo;
	}

	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

	public ArrayList<String> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(ArrayList<String> especialidades) {
		this.especialidades = especialidades;
	}


	@Override
	public String toString() {
		return "ArtistaDTO [idArtista=" + idArtista + ", nombre=" + nombre
				+ ", pais=" + pais + ", apodo=" + apodo + ", especialidades="
				+ especialidades + "]";
	}

	
}
