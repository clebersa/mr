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
import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.text.DvParagraph;
import org.openehr.rm.datatypes.text.DvText;

/**
 *
 * @author cleber
 */
public class DvParagraphImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}
		
		DvParagraph dvParagraph = (DvParagraph) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO dv_paragraph "
					+ "VALUES ('" + key + "')";
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		DvParagraphDvTextImpl dvParagraphDvTextImpl = new DvParagraphDvTextImpl();
		dvParagraphDvTextImpl.saveList(key, dvParagraph.getItems());

		System.out.println("Records created successfully");
	}

	public DataValue get(String key) {
		if(key == null){
			throw new NullPointerException();
		}
		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;

		DvParagraph dvParagraph = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_paragraph "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				DvParagraphDvTextImpl dvParagraphDvTextImpl = new DvParagraphDvTextImpl();
				List<DvText> dvTextList = dvParagraphDvTextImpl.getList(key);
				dvParagraph = new DvParagraph(dvTextList);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return dvParagraph;
	}

	public Map<String, DataValue> search(Map<String, String> criterios) {
		throw new UnsupportedOperationException("Not supported yet."); 
	}
}
