/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.persistencia.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author cleber
 */
public class DvIdentifierInit {

	public static boolean createTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:rm.db");
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "CREATE TABLE dv_identifier "
					+ "(id TEXT PRIMARY KEY NOT NULL,"
					+ " issuer TEXT NOT NULL, "
					+ " assigner TEXT NOT NULL, "
					+ " dv_id TEXT NOT NULL, "
					+ " type TEXT NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
		System.out.println("Table created successfully");
		return true;
	}
}
