/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.persistencia.impl;

import br.ufg.inf.fabrica.persistencia.ConnectionCreator;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.openehr.rm.datatypes.text.TermMapping;

/**
 *
 * @author cleber
 */
public class DvTextTermMappingImpl {

	public void saveList(String key, List<TermMapping> list) {
		if (key == null) {
			throw new NullPointerException();
		}
		if(list == null) return;

		TermMappingImpl termMappingImpl = new TermMappingImpl();
		String termMappingId;
		for (int index = 0; index < list.size(); index++) {
			termMappingId = UUID.randomUUID().toString();
			termMappingImpl.save(termMappingId, list.get(index));
			
			Connection c = ConnectionCreator.getConnection();
			Statement stmt = null;
			try {
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "INSERT INTO dv_text_term_mapping "
						+ "VALUES ('" + key + "'"
						+ ", " + index
						+ ", '" + termMappingId + "')";
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
	}
	
	public List<TermMapping> getList(String dvTextId){
		if(dvTextId == null){
			throw new NullPointerException();
		}
		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		List<TermMapping> list = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_text_term_mapping "
					+ "WHERE dv_text_id = '" + dvTextId + "' ORDER BY position");
			if(rs.getFetchSize() > 0){
				list = new ArrayList<TermMapping>();
				while (rs.next()) {
					TermMappingImpl termMappingImpl = new TermMappingImpl();
					list.add((TermMapping) termMappingImpl.get(
							rs.getString("term_mapping_id")));
				}
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return list;
	}
}
