/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.persistencia.impl;

import br.ufg.inf.fabrica.persistencia.init.CodePhraseInit;
import java.util.UUID;
import junit.framework.TestCase;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.identification.TerminologyID;

/**
 *
 * @author cleber
 */
public class CodePhraseImplTest extends TestCase {
	
	CodePhraseImpl codePhraseImpl;
	
	public CodePhraseImplTest(String testName) {
		super(testName);
		CodePhraseInit.createTable();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		codePhraseImpl = new CodePhraseImpl();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test of save method, of class CodePhraseImpl.
	 */
	public void testSave() {
		System.out.println("save");
		String key = UUID.randomUUID().toString();
		CodePhrase codePhrase = new CodePhrase(new TerminologyID("mname", "1.0"), 
				"teste");
		codePhraseImpl.save(key, codePhrase);
	}

	/**
	 * Test of get method, of class CodePhraseImpl.
	 */
	public void testGet() throws InterruptedException {
		System.out.println("get");
		String key = "75c68ee3-e747-4fca-b7eb-553dcb314d1c";
		CodePhraseImpl instance = new CodePhraseImpl();
		assertNotNull(instance.get(key));
	}
	
}
