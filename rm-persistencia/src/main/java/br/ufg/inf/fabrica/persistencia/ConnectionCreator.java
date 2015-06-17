package br.ufg.inf.fabrica.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Classe responsável por criar conexões com o banco de dados SQLite.
 *
 * @author cleber
 */
public class ConnectionCreator {

	public static String DB_NAME = "rm.db";

	public static Connection getConnection() {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
		System.out.println("Opened database successfully");
		return c;
	}
}
