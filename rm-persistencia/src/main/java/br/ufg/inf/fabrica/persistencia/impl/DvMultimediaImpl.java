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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.encapsulated.DvMultimedia;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.datatypes.uri.DvURI;
import org.openehr.rm.support.terminology.TerminologyService;

/**
 *
 * @author cleber
 */
public class DvMultimediaImpl implements DataValueRepository {

	public void save(String key, DataValue objeto) {
		if(key == null || objeto == null){
			throw new NullPointerException();
		}
		
		DvMultimedia dvMultimedia = (DvMultimedia) objeto;

		Connection c = ConnectionCreator.getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);

			DvEncapsulatedImpl dvEncapsulatedImpl = new DvEncapsulatedImpl();
			String dvEncapsulatedId = UUID.randomUUID().toString();
			dvEncapsulatedImpl.save(dvEncapsulatedId, dvMultimedia);
			
			CodePhraseImpl codePhraseImpl = new CodePhraseImpl();
			
			String mediaTypeId = UUID.randomUUID().toString();
			codePhraseImpl.save(mediaTypeId, dvMultimedia.getMediaType());
			
			String compressionAlgorithmId = UUID.randomUUID().toString();
			codePhraseImpl.save(compressionAlgorithmId, 
					dvMultimedia.getCompressionAlgorithm());
			
			String integrityCheckAlgorithmId = UUID.randomUUID().toString();
			codePhraseImpl.save(integrityCheckAlgorithmId, 
					dvMultimedia.getIntegrityCheckAlgorithm());
			
			DvMultimediaImpl dvMultimediaImpl = new DvMultimediaImpl();
			String thumbnailId = UUID.randomUUID().toString();
			dvMultimediaImpl.save(thumbnailId, dvMultimedia.getThumbnail());
			
			DvURIImpl dvURIImpl = new DvURIImpl();
			String uriId = UUID.randomUUID().toString();
			dvURIImpl.save(uriId, dvMultimedia.getUri());
			
			stmt = c.createStatement();
			String sql = "INSERT INTO dv_multimedia "
					+ "VALUES ('" + key + "'"
					+ ", '" + dvMultimedia.getAlternateText() + "'"
					+ ", '" + mediaTypeId + "'"
					+ ", '" + compressionAlgorithmId + "'"
					+ ", '" + Base64.getEncoder().encodeToString(
							dvMultimedia.getIntegrityCheck()) + "'"
					+ ", '" + integrityCheckAlgorithmId + "'"
					+ ", '" + thumbnailId + "'"
					+ ", '" + uriId + "'"
					+ ", '" + Base64.getEncoder().encodeToString(
							dvMultimedia.getData()) + "'"
					+ ", '" + dvEncapsulatedId + "')";
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
		DvMultimedia object = null;
		try {
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dv_multimedia "
					+ "WHERE id = '" + key + "'");
			while (rs.next()) {
				DvEncapsulatedImpl dvEncapsulatedImpl = new DvEncapsulatedImpl();
				ArrayList<HashMap<String, Object>> result = (ArrayList<HashMap<String, Object>>) dvEncapsulatedImpl.get(
						rs.getString("dv_encapsulated_id"));
				CodePhraseImpl codePhraseImpl = new CodePhraseImpl();
				DvMultimediaImpl dvMultimediaImpl = new DvMultimediaImpl();
				DvURIImpl dvURIImpl = new DvURIImpl();
				object = new DvMultimedia((CodePhrase)result.get(0).get("charset"), 
						(CodePhrase) result.get(0).get("language"), 
						rs.getString("alternate_text"),
						(CodePhrase) codePhraseImpl.get(rs.getString("media_type")),
						(CodePhrase) codePhraseImpl.get(rs.getString("compression_algorithm")),
						Base64.getDecoder().decode(rs.getString("integrity_check")),
						(CodePhrase) codePhraseImpl.get(rs.getString("integrity_check_algorithm")),
						(DvMultimedia) dvMultimediaImpl.get(rs.getString("thumbnail")),
						(DvURI) dvURIImpl.get(rs.getString("uri")),
						Base64.getDecoder().decode(rs.getString("data")),
						(TerminologyService)result.get(0).get("terminology_service"));
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
