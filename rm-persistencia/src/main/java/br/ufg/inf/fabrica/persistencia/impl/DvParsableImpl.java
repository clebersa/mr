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
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.terminology.TerminologyService;

/**
 *
 * @author cleber
 */
public class DvParsableImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}
		
		DvParsable dvParsable = (DvParsable) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			DvEncapsulatedImpl dvEncapsulatedImpl = new DvEncapsulatedImpl();
			String id = UUID.randomUUID().toString();
			dvEncapsulatedImpl.save(id, dvParsable);
			
			stmt = c.createStatement();
			String sql = "INSERT INTO dv_parsable "
					+ "VALUES ('" + key + "'"
					+ ", '" + dvParsable.getValue() + "'"
					+ ", '" + dvParsable.getFormalism() + "'"
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
		DvParsable object = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_parsable "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				DvEncapsulatedImpl dvEncapsulatedImpl = new DvEncapsulatedImpl();
				ArrayList<HashMap<String, Object>> result = (ArrayList<HashMap<String, Object>>) dvEncapsulatedImpl.get(
						rs.getString("dv_encapsulated_id"));
				object = new DvParsable((CodePhrase)result.get(0).get("charset"), 
						(CodePhrase) result.get(0).get("language"), 
						rs.getString("value"), 
						rs.getString("formalism"), 
						(TerminologyService)result.get(0).get("terminology_service"));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return object;
	}

	public Map<String, DataValue> search(Map<String, String> criterios) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
