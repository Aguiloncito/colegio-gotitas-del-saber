package main.java.edu.ingsoft.colegio.gotitas.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author AGUILON
 */
/*
Clase con patrón de diseño singleton.
*/
public class DataBaseConnection {
    //atributos 
    private static Connection connection;
    /*
    el constructor tiene que ser privado, esto para evitar
    que se creen instancias de esta clase
    */
    
    private DataBaseConnection(){}
    //metodo
    public static Connection getConnectionDataBase() throws SQLException{
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(Credentials.URL_DB, Credentials.USER_DB, Credentials.PASS_DB);
        }
        return connection;
    }
}
