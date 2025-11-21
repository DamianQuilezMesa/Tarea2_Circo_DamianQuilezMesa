package modelo;

public class Sesion {
    public Credenciales credenciales = null;
    public Perfiles perfil = Perfiles.INVITADO;
    private Long idArtista; // NUEVO CAMPO

    public Sesion() {
    }

    public Sesion(Credenciales credenciales, Perfiles perfil) {
        this.credenciales = credenciales;
        this.perfil = perfil;
    }

    public void iniciarSesionPerfiles(Credenciales credenciales) {
        this.credenciales = credenciales;
        this.perfil = credenciales.getPerfil();
    }

    public void iniciarSesionAdmin(String nombreUsuario, Perfiles perfil) {
        this.credenciales = new Credenciales(null, nombreUsuario, null, perfil);
        this.perfil = perfil;
    }

    public void cerrarSesion() {
        this.credenciales = null;
        this.perfil = Perfiles.INVITADO;
        this.idArtista = null;
    }

    public void cerrarApp() {
        this.credenciales = null;
        this.perfil = null;
        this.idArtista = null;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public Perfiles getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfiles perfil) {
        this.perfil = perfil;
    }

    // NUEVOS MÉTODOS
    public Long getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(Long idArtista) {
        this.idArtista = idArtista;
    }

    @Override
    public String toString() {
        return "Sesion [credenciales=" + credenciales + ", perfil=" + perfil
                + ", idArtista=" + idArtista + "]";
    }
}
