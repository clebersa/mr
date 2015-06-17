/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.persistencia.impl;

import br.ufg.inf.fabrica.persistencia.init.TerminologyIdInit;
import java.util.UUID;
import junit.framework.TestCase;
import org.openehr.rm.support.identification.TerminologyID;

/**
 *
 * @author cleber
 */
public class TerminologyIdImplTest extends TestCase {
	
	TerminologyIdImpl terminologyIdImpl;
	
	public TerminologyIdImplTest(String testName) {
		super(testName);
		TerminologyIdInit.createTable();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		terminologyIdImpl = new TerminologyIdImpl();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test of save method, of class TerminologyIdImpl.
	 */
	public void testSave() {
		System.out.println("save");
		String key = UUID.randomUUID().toString();
		TerminologyID terminologyID = new TerminologyID("mname", "1.0");
		terminologyIdImpl.save(key, terminologyID);
	}
	
	public void testSaveValueNull() {
		System.out.println("save");
		String key = UUID.randomUUID().toString();
		TerminologyID terminologyID = new TerminologyID("mname", null);
		terminologyIdImpl.save(key, terminologyID);
	}

	/**
	 * Test of get method, of class TerminologyIdImpl.
	 */
	public void testGet() throws InterruptedException {
		System.out.println("get");
		String key = "a4292196-9100-4199-a743-8143fa5a6949";
		TerminologyIdImpl instance = new TerminologyIdImpl();
		assertNotNull(instance.get(key));
	}
	
}
