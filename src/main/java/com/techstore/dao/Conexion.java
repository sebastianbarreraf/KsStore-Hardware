package com.techstore.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexión a la base de datos. Cambia los valores por defecto o define las
 * variables de entorno DB_URL, DB_USER y DB_PASS.
 */
public final class Conexion {
    private static final String URL  = env("DB_URL",  "jdbc:mariadb://localhost:3306/tienda_hardware");
    private static final String USER = env("DB_USER", "tienda_app");
    private static final String PASS = env("DB_PASS", "TuClaveSegura123");

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Conexion() {}

    private static String env(String clave, String porDefecto) {
        String v = System.getenv(clave);
        return v != null ? v : porDefecto;
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
