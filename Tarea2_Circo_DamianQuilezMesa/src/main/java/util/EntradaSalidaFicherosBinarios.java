/**
* Clase EntradaSalidaFicherosBinarios.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntradaSalidaFicherosBinarios {
	
	
	 /**
     * Escribe una lista de objetos serializables desde un fichero binario (.dat)
     * T es un tipo genérico, no debería necesitar sustituirlo con el objeto x
     */
	public static <T extends Serializable> void escribirLista(String ruta, List<T> lista) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(lista);
        }
    }

    /**
     * Lee una lista de objetos serializables desde un fichero binario (.dat)
     */

    public static <T extends Serializable> List<T> leerLista(String ruta) throws IOException, ClassNotFoundException {

        File f = new File(ruta);
        if (!f.exists()) {
            return new ArrayList<>(); // fichero no existe → devolvemos lista vacía
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return (List<T>) ois.readObject();
        }
    }

}
