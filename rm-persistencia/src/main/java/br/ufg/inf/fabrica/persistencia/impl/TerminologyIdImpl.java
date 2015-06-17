/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.persistencia.impl;

import br.ufg.inf.fabrica.persistencia.ConnectionCreator;
import br.ufg.inf.fabrica.persistencia.GeneralRepository;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.openehr.rm.support.identification.TerminologyID;

/**
 *
 * @author cleber
 */
public class TerminologyIdImpl implements GeneralRepository {

	public void save(String key, Object objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}
		
		TerminologyID terminologyID = (TerminologyID) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO terminology_id "
					+ "VALUES ('" + key + "', '"
					+ terminologyID.name() + "', '"
					+ terminologyID.versionID() + "')";
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		System.out.println("Records created successfully");
	}

	public Object get(String key) {
		if(key == null){
			throw new NullPointerException();
		}
		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		TerminologyID object = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM terminology_id "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				object = new TerminologyID(rs.getString("name"), 
						rs.getString("version"));
				System.out.println("Terminology ID = {" + object.name() 
						+ ", " + object.versionID()+"}");
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return object;
	}
}
