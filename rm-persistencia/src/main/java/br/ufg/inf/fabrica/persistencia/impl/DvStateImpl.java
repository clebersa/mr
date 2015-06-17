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
import org.openehr.rm.datatypes.basic.DvState;
import org.openehr.rm.datatypes.encapsulated.DvParsable;
import org.openehr.rm.datatypes.text.DvCodedText;

/**
 *
 * @author cleber
 */
public class DvStateImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}
		
		DvState dvState = (DvState) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			DvCodedTextImpl dvCodedTextImpl = new DvCodedTextImpl();
			String id = UUID.randomUUID().toString();
			dvCodedTextImpl.save(id, dvState.getValue());
			
			stmt = c.createStatement();
			String sql = "INSERT INTO dv_state "
					+ "VALUES ('" + key + "'"
					+ ", '" + id + "'"
					+ ", '" + (dvState.isTerminal()?1:0) + "')";
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

		DvState dvState = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_state "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				DvCodedTextImpl dvCodedTextImpl = new DvCodedTextImpl();
				dvState = new DvState((DvCodedText) dvCodedTextImpl.get(rs.getString("value")), 
						(rs.getInt("terminal") == 1));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return dvState;
	}

	public Map<String, DataValue> search(Map<String, String> criterios) {
		throw new UnsupportedOperationException("Not supported yet."); 
	}
}
