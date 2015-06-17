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
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.encapsulated.DvEncapsulated;
import org.openehr.terminology.SimpleTerminologyService;

/**
 *
 * @author cleber
 */
public class DvEncapsulatedImpl implements GeneralRepository {

	public void save(String key, Object objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}

		DvEncapsulated dvEncapsulated = (DvEncapsulated) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			CodePhraseImpl codePhraseImpl = new CodePhraseImpl();
			String languageId = null;
			if(dvEncapsulated.getLanguage()!= null){
				languageId = UUID.randomUUID().toString();
				codePhraseImpl.save(languageId, dvEncapsulated.getLanguage());
			}
			String charsetId = null;
			if(dvEncapsulated.getCharset() != null){
				charsetId = UUID.randomUUID().toString();
				codePhraseImpl.save(charsetId, dvEncapsulated.getCharset());
			}
			
			stmt = c.createStatement();
			String sql = "INSERT INTO code_phrase "
					+ "VALUES ('" + key + "'";
			if(languageId == null){
				sql += ", null";
			}else{
				sql += ", '" + languageId + "'";
			}
			if(charsetId == null){
				sql += ", null";
			}else{
				sql += ", '" + charsetId + "'";
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
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_encapsulated "
					+ "WHERE id = '" + key + "'");
			HashMap<String, Object> line;
			while (rs.next()) {
				line = new HashMap<String, Object>();
				CodePhraseImpl codePhraseImpl = new CodePhraseImpl();
				if(StringUtils.isEmpty(rs.getString("language"))){
					line.put("language", null);
				}else{
					line.put("language", codePhraseImpl.get(rs.getString("language")));
				}
				if(StringUtils.isEmpty(rs.getString("charset"))){
					line.put("charset", null);
				}else{
					line.put("charset", codePhraseImpl.get(rs.getString("charset")));
				}
				//Só tem uma classe que implementa TerminologyService, que é a
				//SimpleTerminologyService. Posso retornar sempre uma instância
				//dela?
				line.put("terminology_service", SimpleTerminologyService.getInstance());
				/* if(StringUtils.isEmpty(rs.getString("terminology_service_class"))){
					line.put("terminology_service", null);
				}else{
					if(SimpleTerminologyService.class.getName().equals(
							rs.getString("terminology_service_class"))){
						line.put("terminology_service", SimpleTerminologyService.getInstance());
					}else{
						line.put("terminology_service", null);
					}
					line.put("charset", codePhraseImpl.get(rs.getString("charset")));
				}*/
				result.add(line);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return result;
	}

	public Map<String, DataValue> search(Map<String, String> criterios) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
