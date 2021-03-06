package br.ufg.inf.fabrica.persistencia;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.datatypes.uri.DvURI;
import org.openehr.rm.support.identification.TerminologyID;

import java.util.UUID;

/**
 * Testes com DvURI.
 */
public class CodePhraseRepositoryTest extends TestCase {

    private DataValueRepository repo;

    @Override
    public void setUp() {
        // Obtém repositório

        repo = new DataValueRepositoryRam();
    }

    public void testPersisteRecupera() {
        TerminologyID ti = new TerminologyID("cid-10");
        CodePhrase cp = new CodePhrase(ti, "cid");

        String key = UUID.randomUUID().toString();

        repo.save(key, cp);

        Assert.assertEquals(cp, repo.get(key));
    }
}
