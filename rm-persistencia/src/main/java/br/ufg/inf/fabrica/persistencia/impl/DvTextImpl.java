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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.datatypes.text.DvText;
import org.openehr.rm.datatypes.text.TermMapping;
import org.openehr.rm.datatypes.uri.DvURI;
import org.openehr.terminology.SimpleTerminologyService;

/**
 *
 * @author cleber
 */
public class DvTextImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if (key == null || objeto == null) {
			throw new NullPointerException();
		}

		DvText dvText = (DvText) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			DvEncapsulatedImpl dvEncapsulatedImpl = new DvEncapsulatedImpl();
			String dvEncapsulatedId = UUID.randomUUID().toString();
			dvEncapsulatedImpl.save(dvEncapsulatedId, dvText);

			CodePhraseImpl codePhraseImpl = new CodePhraseImpl();

			String languageId = UUID.randomUUID().toString();
			codePhraseImpl.save(languageId, dvText.getLanguage());

			String charsetId = UUID.randomUUID().toString();
			codePhraseImpl.save(charsetId,
					dvText.getEncoding());

			DvURIImpl dvURIImpl = new DvURIImpl();
			String hyperlinkId = UUID.randomUUID().toString();
			dvURIImpl.save(hyperlinkId, dvText.getHyperlink());

			stmt = c.createStatement();
			String sql = "INSERT INTO dv_text "
					+ "VALUES ('" + key + "'"
					+ ", '" + dvText.getValue() + "'"
					+ ", '" + dvText.getFormatting() + "'"
					+ ", '" + hyperlinkId + "'"
					+ ", '" + languageId + "'"
					+ ", '" + charsetId + "'"
					+ ", null, null)";
			stmt.executeUpdate(sql);

			DvTextTermMappingImpl dvTextTermMappingImpl = new DvTextTermMappingImpl();
			dvTextTermMappingImpl.saveList(key, dvText.getMappings());

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
		DvText object = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_text "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				CodePhraseImpl codePhraseImpl = new CodePhraseImpl();
				DvURIImpl dvURIImpl = new DvURIImpl();
				DvTextTermMappingImpl dvTextTermMappingImpl = new DvTextTermMappingImpl();
				List<TermMapping> termMappingList = dvTextTermMappingImpl.getList(key);

				object = new DvText(rs.getString("value"),
						termMappingList,
						rs.getString("formatting"),
						(DvURI) dvURIImpl.get(rs.getString("hyperlink")),
						(CodePhrase) codePhraseImpl.get(rs.getString("language")),
						(CodePhrase) codePhraseImpl.get(rs.getString("charset")),
						SimpleTerminologyService.getInstance());
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
