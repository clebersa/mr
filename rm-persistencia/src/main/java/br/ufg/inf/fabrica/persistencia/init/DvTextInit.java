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
public class DvTextInit {
	
	public static boolean createTable(){
	Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:rm.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE dv_text "
                    + "(id TEXT PRIMARY KEY NOT NULL,"
                    + " value TEXT NOT NULL, "
                    + " formatting TEXT, "
                    + " hyperlink TEXT, "
                    + " language TEXT, "
                    + " charset TEXT, "
                    + " terminology_service_class TEXT, "
                    + " terminology_service_id TEXT)";
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
