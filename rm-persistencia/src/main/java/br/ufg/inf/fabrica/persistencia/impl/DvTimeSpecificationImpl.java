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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.openehr.rm.datatypes.encapsulated.DvParsable;
import org.openehr.rm.datatypes.timespec.DvTimeSpecification;

/**
 *
 * @author cleber
 */
public class DvTimeSpecificationImpl implements GeneralRepository {

	public void save(String key, Object objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}
		
		DvTimeSpecification dvTimeSpecification = (DvTimeSpecification) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			DvParsableImpl dvParsableImpl = new DvParsableImpl();
			String id = UUID.randomUUID().toString();
			dvParsableImpl.save(id, dvTimeSpecification.getValue());
			
			stmt = c.createStatement();
			String sql = "INSERT INTO dv_time_specification "
					+ "VALUES ('" + key + "'"
					+ ", '" + id + "')";
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

		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_time_specification "
					+ "WHERE id = '" + key + "'");
			
			HashMap<String, Object> line;
			while (rs.next()) {
				line = new HashMap<String, Object>();
				
				DvParsableImpl dvParsableImpl = new DvParsableImpl();
				DvParsable dvParsable = (DvParsable) dvParsableImpl.get(
						rs.getString("value"));
				line.put("value", dvParsable);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return result;
	}
}
