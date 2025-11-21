/**
* Clase CoordinacionDTO.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package dto;

import java.time.LocalDate;

public class CoordinacionDTO {
	private Long idCoord;
	private Long idPersona;
	private String nombre;
	private String email;
	private boolean senior;
	private LocalDate fechaSenior;

	public CoordinacionDTO() {
	}

	public CoordinacionDTO(Long idCoord, Long idPersona, String nombre,
			String email, boolean senior, LocalDate fechaSenior) {
		this.idCoord = idCoord;
		this.idPersona = idPersona;
		this.nombre = nombre;
		this.email = email;
		this.senior = senior;
		this.fechaSenior = fechaSenior;
	}

	public Long getIdCoord() {
		return idCoord;
	}

	public void setIdCoord(Long idCoord) {
		this.idCoord = idCoord;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isSenior() {
		return senior;
	}

	public void setSenior(boolean senior) {
		this.senior = senior;
	}

	public LocalDate getFechaSenior() {
		return fechaSenior;
	}

	public void setFechaSenior(LocalDate fechaSenior) {
		this.fechaSenior = fechaSenior;
	}

	@Override
	public String toString() {
		return "CoordinacionDTO [idCoord=" + idCoord + ", idPersona="
				+ idPersona + ", nombre=" + nombre + ", email=" + email
				+ ", senior=" + senior + ", fechaSenior=" + fechaSenior + "]";
	}

	

}
