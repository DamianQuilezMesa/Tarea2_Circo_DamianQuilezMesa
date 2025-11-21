/**
* Clase InformeEspectaculo.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

/**
 * Clase InformeEspectaculo.java
 *
 * DTO compuesto que agrupa todos los datos del informe de un espectáculo:
 * - Datos del espectáculo
 * - Coordinador
 * - Lista de números con sus artistas
 *
 * @author Damián
 * @version 2.1
 */

package dto;

import java.util.ArrayList;
import java.util.List;

public class InformeEspectaculo {
	private EspectaculoDTO espectaculo;
	private CoordinacionDTO coordinador; // puede ser null
	private List<NumeroDTO> numeros; // nunca null (lista vacía si no hay)

	// Constructor vacío: inicializa lista para evitar NullPointerException
	public InformeEspectaculo() {
		this.numeros = new ArrayList<>();
	}

	// Constructor completo
	public InformeEspectaculo(EspectaculoDTO espectaculo,
			CoordinacionDTO coordinador, List<NumeroDTO> numeros) {
		this.espectaculo = espectaculo;
		this.coordinador = coordinador;
		this.numeros = (numeros != null) ? numeros : new ArrayList<>();
	}

	public EspectaculoDTO getEspectaculo() {
		return espectaculo;
	}

	public void setEspectaculo(EspectaculoDTO espectaculo) {
		this.espectaculo = espectaculo;
	}

	public CoordinacionDTO getCoordinador() {
		return coordinador;
	}

	public void setCoordinador(CoordinacionDTO coordinador) {
		this.coordinador = coordinador;
	}

	public List<NumeroDTO> getNumeros() {
		return numeros;
	}

	public void setNumeros(List<NumeroDTO> numeros) {
		this.numeros = (numeros != null) ? numeros : new ArrayList<>();
	}

	@Override
	public String toString() {
		return "InformeEspectaculo{" + "espectaculo=" + espectaculo
				+ ", coordinador=" + coordinador + ", numeros=" + numeros + '}';
	}
}
