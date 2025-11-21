/**
* Clase EspectaculoDTO.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package dto;

import java.time.LocalDate;

public class EspectaculoDTO {
	private long idEspectaculo;
	private String nombreEsp;
	private LocalDate fechaIni;
	private LocalDate fechaFin;
	private long idCoord; // referencia al coordinador

	public EspectaculoDTO() {
	}

	public EspectaculoDTO(long idEspectaculo, String nombreEsp,
			LocalDate fechaIni, LocalDate fechaFin, long idCoord) {
		this.idEspectaculo = idEspectaculo;
		this.nombreEsp = nombreEsp;
		this.fechaIni = fechaIni;
		this.fechaFin = fechaFin;
		this.idCoord = idCoord;
	}

	public long getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}

	public String getNombreEsp() {
		return nombreEsp;
	}

	public void setNombreEsp(String nombreEsp) {
		this.nombreEsp = nombreEsp;
	}

	public LocalDate getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(LocalDate fechaIni) {
		this.fechaIni = fechaIni;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public long getIdCoord() {
		return idCoord;
	}

	public void setIdCoord(long idCoord) {
		this.idCoord = idCoord;
	}

	@Override
	public String toString() {
		return "Espectaculo Nº "+ idEspectaculo
				+ ", nombreEsp='" + nombreEsp + '\'' + ", fechaIni=" + fechaIni
				+ ", fechaFin=" + fechaFin + ", idCoord=" + idCoord ;
	}
}
