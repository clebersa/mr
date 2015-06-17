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
import java.util.Map;
import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.basic.DvBoolean;

/**
 *
 * @author cleber
 */
public class DvBooleanImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if (key == null || objeto == null) {
			throw new NullPointerException();
		}

		DvBoolean dvBoolean = (DvBoolean) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO dv_boolean "
					+ "VALUES ('" + key + "'"
					+ ", '" + (dvBoolean.getValue() ? 1 : 0) + "')";
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
		if (key == null) {
			throw new NullPointerException();
		}
		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;

		DvBoolean dvBoolean = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_boolean "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				dvBoolean = new DvBoolean(rs.getInt("value") == 1);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return dvBoolean;
	}

	public Map<String, DataValue> search(Map<String, String> criterios) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
