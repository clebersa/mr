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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.datatypes.text.DvCodedText;
import org.openehr.rm.datatypes.text.Match;
import org.openehr.rm.datatypes.text.TermMapping;
import org.openehr.terminology.SimpleTerminologyService;

/**
 *
 * @author cleber
 */
public class TermMappingImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}

		TermMapping termMapping = (TermMapping) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			CodePhraseImpl codePhraseImpl = new CodePhraseImpl();
			String targetId = null;
			targetId = UUID.randomUUID().toString();
			codePhraseImpl.save(targetId, termMapping.getTarget());

			DvTextImpl dvTextImpl = new DvTextImpl();
			String dvTextId = null;
			if(termMapping.getPurpose() != null){
				dvTextId = UUID.randomUUID().toString();
				dvTextImpl.save(dvTextId, termMapping.getPurpose());
			}
			
			stmt = c.createStatement();
			String sql = "INSERT INTO term_mapping "
					+ "VALUES ('" + key + "'"
					+ ", '" + targetId + "'"
					+ ", '" + termMapping.getMatch().getValue() + "'";
			if(dvTextId == null){
				sql += ", null";
			}else{
				sql += ", '" + dvTextId + "'";
			}		
			sql += ", null, null)";
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
		
		TermMapping termMapping = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM term_mapping "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				CodePhraseImpl codePhraseImpl = new CodePhraseImpl();
				DvCodedTextImpl dvCodedTextImpl = new DvCodedTextImpl();
				termMapping = new TermMapping(
						(CodePhrase)codePhraseImpl.get(rs.getString("target")), 
						Match.valueOf(rs.getString("match")), 
						(DvCodedText) dvCodedTextImpl.get(rs.getString("purpose")),
						SimpleTerminologyService.getInstance());
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return termMapping;
	}

	public Map<String, DataValue> search(Map<String, String> criterios) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
