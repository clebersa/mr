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
import org.openehr.rm.datatypes.text.DvText;
import org.openehr.rm.datatypes.text.DvText;

/**
 *
 * @author cleber
 */
public class DvParagraphDvTextImpl {

	public void saveList(String key, List<DvText> list) {
		if (key == null) {
			throw new NullPointerException();
		}
		if(list == null) return;

		DvTextImpl dvTextImpl = new DvTextImpl();
		String dvTextId;
		for (int index = 0; index < list.size(); index++) {
			dvTextId = UUID.randomUUID().toString();
			dvTextImpl.save(dvTextId, list.get(index));
			
			Connection c = ConnectionCreator.getConnection();
			Statement stmt = null;
			try {
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "INSERT INTO dv_paragraph_dv_text "
						+ "VALUES ('" + key + "'"
						+ ", " + index
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
	}
	
	public List<DvText> getList(String dvParagraphId){
		if(dvParagraphId == null){
			throw new NullPointerException();
		}
		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		List<DvText> list = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_paragraph_dv_text "
					+ "WHERE dv_paragraph = '" + dvParagraphId + "' ORDER BY position");
			if(rs.getFetchSize() > 0){
				list = new ArrayList<DvText>();
				while (rs.next()) {
					DvTextImpl dvTextImpl = new DvTextImpl();
					list.add((DvText) dvTextImpl.get(
							rs.getString("dv_text_id")));
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
