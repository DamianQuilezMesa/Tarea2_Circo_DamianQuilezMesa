/**
* Clase PersonaDTO.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package dto;

import modelo.Perfiles;

public class PersonaDTO {

	private Long id;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String email;
	private String pais;
	// Datos credenciales
	private String nomUsuario;
	private String contrasena;
	private Perfiles perfil;

	public PersonaDTO() {

	}

	public PersonaDTO(String nombre, String apellido1, String apellido2,
			String email, String pais, String nomUsuario, String contrasena,
			Perfiles perfil) {
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.email = email;
		this.pais = pais;
		this.nomUsuario = nomUsuario;
		this.contrasena = contrasena;
		this.perfil = perfil;
	}

	public PersonaDTO(Long id, String nombre, String apellido1,
			String apellido2, String email, String pais, String nomUsuario,
			String contrasena, Perfiles perfil) {
		this.id = id;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.email = email;
		this.pais = pais;
		this.nomUsuario = nomUsuario;
		this.contrasena = contrasena;
		this.perfil = perfil;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getNomUsuario() {
		return nomUsuario;
	}

	public void setNomUsuario(String nomUsuario) {
		this.nomUsuario = nomUsuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Perfiles getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfiles perfil) {
		this.perfil = perfil;
	}

	@Override
	public String toString() {
		return "PersonaDTO [id=" + id + ", nombre=" + nombre + ", apellido1="
				+ apellido1 + ", apellido2=" + apellido2 + ", email=" + email
				+ ", nacionalidad=" + pais + ", nombreUsuario=" + nomUsuario
				+ ", password=" + contrasena + ", perfil=" + perfil + "]";
	}

}
