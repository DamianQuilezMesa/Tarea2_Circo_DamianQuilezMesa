package modelo;

public class Credenciales {

	private Long id;
	private String nomUsuario;
	private String contrasena;
	private Perfiles perfil;

	public Credenciales() {
	}

	public Credenciales(Long id, String nomUsuario, String password,
			Perfiles perfil) {
		this.id = id;
		this.nomUsuario = nomUsuario;
		this.contrasena = password;
		this.perfil = perfil;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return "Credenciales [id=" + id + ", nombre=" + nomUsuario
				+ ", password=" + contrasena + ", perfil=" + perfil + "]";
	}

}
