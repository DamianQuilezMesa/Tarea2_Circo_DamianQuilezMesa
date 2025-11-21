/**
* Clase conexionBBDD.java
*
*@author Damián Quílez Mesa
*@version 1.0
*/

package bbdd;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;

public class ConexionBBDD {

	private static ConexionBBDD instance;
	private MysqlDataSource dataSource;

	private ConexionBBDD() {
		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				"src/main/resources/application.properties")) {
			prop.load(fis);
			dataSource = new MysqlDataSource();
			dataSource.setURL(prop.getProperty("url"));
			dataSource.setUser(prop.getProperty("usuBBDD"));
			dataSource.setPassword(prop.getProperty("passBBDD"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized ConexionBBDD getInstance() {
		if (instance == null) {
			instance = new ConexionBBDD();
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {

		return dataSource.getConnection();
	}
}
