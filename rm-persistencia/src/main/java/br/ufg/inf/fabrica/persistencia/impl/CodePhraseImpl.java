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
import java.util.UUID;
import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.identification.TerminologyID;

/**
 *
 * @author cleber
 */
public class CodePhraseImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}
		
		CodePhrase codePhrase = (CodePhrase) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			TerminologyIdImpl terminologyIdImpl = new TerminologyIdImpl();
			String id = UUID.randomUUID().toString();
			terminologyIdImpl.save(id, codePhrase.getTerminologyId());
			
			stmt = c.createStatement();
			String sql = "INSERT INTO code_phrase "
					+ "VALUES ('" + key + "', '"
					+ id + "', '"
					+ codePhrase.getCodeString() + "')";
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
		CodePhrase object = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM code_phrase "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				TerminologyIdImpl terminologyIdImpl = new TerminologyIdImpl();
				TerminologyID terminologyID = (TerminologyID) terminologyIdImpl.get(
						rs.getString("terminology_id_id"));
				object = new CodePhrase(terminologyID, rs.getString("code_string"));
				System.out.println("CodePhrase = {" + object.getTerminologyId()
						+ ", " + object.getCodeString()+"}");
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
