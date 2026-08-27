package main.java.edu.ingsoft.colegio.gotitas.config;

/**
 *
 * @author AGUILON
 */
public class Credentials {
    public static final String DATA_BASE = System.getenv("DATA_BASE");
    public static final String URL_DB = System.getenv("URL_MYSQL_DB")+DATA_BASE;
    public static final String USER_DB = System.getenv("USER_MYSQL_DB");
    public static final String PASS_DB = System.getenv("PASS_DB");
}
