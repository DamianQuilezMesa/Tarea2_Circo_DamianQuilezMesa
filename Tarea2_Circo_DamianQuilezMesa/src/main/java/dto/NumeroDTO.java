/**
* Clase NumeroDTO.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package dto;

import java.util.List;

public class NumeroDTO {
	private long idNumero;
	private String nombreNumero;
	private double duracion;
	private int orden;
	private List<ArtistaDTO> artistas; // relación N:M con artistas

	public NumeroDTO() {
	}

	public NumeroDTO(long idNumero, String nombreNumero, double duracion,
			int orden, List<ArtistaDTO> artistas) {
		this.idNumero = idNumero;
		this.nombreNumero = nombreNumero;
		this.duracion = duracion;
		this.orden = orden;
		this.artistas = artistas;
	}

	public long getIdNumero() {
		return idNumero;
	}

	public void setIdNumero(long idNumero) {
		this.idNumero = idNumero;
	}

	public String getNombreNumero() {
		return nombreNumero;
	}

	public void setNombreNumero(String nombreNumero) {
		this.nombreNumero = nombreNumero;
	}

	public double getDuracion() {
		return duracion;
	}

	public void setDuracion(double duracion) {
		this.duracion = duracion;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public List<ArtistaDTO> getArtistas() {
		return artistas;
	}

	public void setArtistas(List<ArtistaDTO> artistas) {
		this.artistas = artistas;
	}

	@Override
	public String toString() {
		return "NumeroDTO{" + "idNumero=" + idNumero + ", nombreNumero='"
				+ nombreNumero + '\'' + ", duracion=" + duracion + ", orden="
				+ orden + ", artistas=" + artistas + '}';
	}
}
