package br.ufg.inf.fabrica.persistencia.init;

/**
 *
 * @author cleber
 */
public class DatabaseBuilder {
	
	public static void main(String[] args) {
		DatabaseBuilder databaseBuilder = new DatabaseBuilder();
		databaseBuilder.buildDatabase();
	}

	public void buildDatabase(){
		CodePhraseInit.createTable();
		DvBooleanInit.createTable();
		DvCodedTextInit.createTable();
		DvEHRURIInit.createTable();
		DvEncapsulatedInit.createTable();
		DvGeneralTimeSpecificationInit.createTable();
		DvIdentifierInit.createTable();
		DvMultimediaInit.createTable();
		DvParagraDvTextInit.createTable();
		DvParsableInit.createTable();
		DvPeriodicTimeSpecificationInit.createTable();
		DvStateInit.createTable();
		DvTextInit.createTable();
		DvTextTermMappingInit.createTable();
		DvTimeSpecificationInit.createTable();
		DvURIInit.createTable();
		TermMappingInit.createTable();
		TerminologyIdInit.createTable();
	}
	
}
