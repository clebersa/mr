/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.persistencia.impl;

import br.ufg.inf.fabrica.persistencia.ConnectionCreator;
import br.ufg.inf.fabrica.persistencia.DataValueRepository;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.encapsulated.DvParsable;
import org.openehr.rm.datatypes.timespec.DvPeriodicTimeSpecification;

/**
 *
 * @author cleber
 */
public class DvPeriodicTimeSpecificationImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}
		
		DvPeriodicTimeSpecification dvTimeSpecification = (DvPeriodicTimeSpecification) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			DvTimeSpecificationImpl dvTimeSpecificationImpl = new DvTimeSpecificationImpl();
			String id = UUID.randomUUID().toString();
			dvTimeSpecificationImpl.save(id, dvTimeSpecification);
			
			stmt = c.createStatement();
			String sql = "INSERT INTO dv_periodic_time_specification "
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

	public DataValue get(String key) {
		if(key == null){
			throw new NullPointerException();
		}
		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;

		DvPeriodicTimeSpecification dvPeriodicTimeSpecification = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_periodic_time_specification "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				DvTimeSpecificationImpl dvTimeSpecificationImpl = new DvTimeSpecificationImpl();
				ArrayList<HashMap<String, Object>> result = (ArrayList<HashMap<String, Object>>) 
						dvTimeSpecificationImpl.get(
								rs.getString("dv_time_specification_id")
						);
				dvPeriodicTimeSpecification = new DvPeriodicTimeSpecification(
						(DvParsable)result.get(0).get("value")
				);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return dvPeriodicTimeSpecification;
	}

	public Map<String, DataValue> search(Map<String, String> criterios) {
		throw new UnsupportedOperationException("Not supported yet."); 
	}
}
