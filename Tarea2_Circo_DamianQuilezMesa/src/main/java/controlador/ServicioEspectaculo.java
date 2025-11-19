/**
* Clase EspectaculoService.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package controlador;

import java.util.List;
import java.util.Set;

import dao.EspectaculoDAO;
import modelo.Espectaculo;

public class ServicioEspectaculo {
	
	    private EspectaculoDAO espectaculoDAO = new EspectaculoDAO();

	    public List<Espectaculo> listarEspectaculosBasico() {
	        return espectaculoDAO.obtenerEspectaculosBasico();
	    }
	

}
