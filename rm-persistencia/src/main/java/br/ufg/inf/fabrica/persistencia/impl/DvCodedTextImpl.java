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
import org.openehr.rm.datatypes.text.DvCodedText;
import org.openehr.rm.datatypes.text.DvText;
import org.openehr.terminology.SimpleTerminologyService;

/**
 *
 * @author cleber
 */
public class DvCodedTextImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}

		DvCodedText dvCodedText = (DvCodedText) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			DvTextImpl dvTextImpl = new DvTextImpl();
			String dvTextId = UUID.randomUUID().toString();
			dvTextImpl.save(dvTextId, dvCodedText);

			CodePhraseImpl codePhraseImpl = new CodePhraseImpl();
			String codePhraseId = UUID.randomUUID().toString();
			codePhraseImpl.save(codePhraseId, dvCodedText.getDefiningCode());
			
			stmt = c.createStatement();
			String sql = "INSERT INTO term_mapping "
					+ "VALUES ('" + key + "'"
					+ ", '" + codePhraseId + "'"
					+ ", '" + dvTextId + "')";
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
		
		DvCodedText dvCodedText = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_coded_text "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				CodePhraseImpl codePhraseImpl = new CodePhraseImpl();
				DvTextImpl dvTextImpl = new DvTextImpl();
				DvText dvText = (DvText) dvTextImpl.get(rs.getString("dv_text_id"));
				dvCodedText = new DvCodedText(
						dvText.getValue(),
						dvText.getMappings(),
						dvText.getFormatting(),
						dvText.getHyperlink(),
						dvText.getLanguage(),
						dvText.getEncoding(), 
						(CodePhrase) codePhraseImpl.get(rs.getString("defining_code")),
						SimpleTerminologyService.getInstance());
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return dvCodedText;
	}

	public Map<String, DataValue> search(Map<String, String> criterios) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
