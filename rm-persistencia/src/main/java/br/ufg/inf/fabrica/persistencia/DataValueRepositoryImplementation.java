package br.ufg.inf.fabrica.persistencia;

import br.ufg.inf.fabrica.persistencia.impl.CodePhraseImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvBooleanImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvCodedTextImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvEHRURIImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvGeneralTimeSpecificationImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvIdentifierImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvMultimediaImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvParagraphImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvParsableImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvPeriodicTimeSpecificationImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvStateImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvTextImpl;
import br.ufg.inf.fabrica.persistencia.impl.DvURIImpl;
import br.ufg.inf.fabrica.persistencia.impl.TermMappingImpl;
import java.util.HashMap;
import java.util.Map;
import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.basic.DvBoolean;
import org.openehr.rm.datatypes.basic.DvIdentifier;
import org.openehr.rm.datatypes.basic.DvState;
import org.openehr.rm.datatypes.encapsulated.DvMultimedia;
import org.openehr.rm.datatypes.encapsulated.DvParsable;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.datatypes.text.DvCodedText;
import org.openehr.rm.datatypes.text.DvParagraph;
import org.openehr.rm.datatypes.text.DvText;
import org.openehr.rm.datatypes.text.TermMapping;
import org.openehr.rm.datatypes.timespec.DvGeneralTimeSpecification;
import org.openehr.rm.datatypes.timespec.DvPeriodicTimeSpecification;
import org.openehr.rm.datatypes.uri.DvEHRURI;
import org.openehr.rm.datatypes.uri.DvURI;

/**
 *
 * @author cleber
 */
public class DataValueRepositoryImplementation implements DataValueRepository {

	private Map<Class, DataValueRepository> classesMap = new HashMap<Class, 
			DataValueRepository>();

	public void save(String key, DataValue objeto) {
		fillClassesMap();
		classesMap.get(objeto.getClass()).save(key, objeto);
	}

	public DataValue get(String key) {
		fillClassesMap();
		DataValue dataValue = null;
		for(DataValueRepository dataValueRepository : classesMap.values()){
			dataValue = dataValueRepository.get(key);
			if(dataValue != null) break;
		}
		return dataValue;
	}

	public Map<String, DataValue> search(Map<String, String> criterios) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void fillClassesMap() {
		classesMap = new HashMap<Class, DataValueRepository>();
		try {
			classesMap.put(CodePhrase.class,
					CodePhraseImpl.class.newInstance());
			//classesMap.put(DvAbsoluteQuantity.class, DvAbsoluteQuantityImpl.class.newInstance());
			classesMap.put(DvBoolean.class,
					DvBooleanImpl.class.newInstance());
			classesMap.put(DvCodedText.class,
					DvCodedTextImpl.class.newInstance());
			classesMap.put(DvEHRURI.class,
					DvEHRURIImpl.class.newInstance());
			//classesMap.put(DvEncapsulated.class, DvEncapsulatedImpl.class.newInstance()); //Esta classe e abstrata.
			classesMap.put(DvGeneralTimeSpecification.class,
					DvGeneralTimeSpecificationImpl.class.newInstance());
			classesMap.put(DvIdentifier.class,
					DvIdentifierImpl.class.newInstance());
			//classesMap.put(DvInterval.class, DvIntervalImpl.class.newInstance());
			classesMap.put(DvMultimedia.class,
					DvMultimediaImpl.class.newInstance());
			//classesMap.put(DvOrdered.class, DvOrderedImpl.class.newInstance());
			//classesMap.put(DvOrdinal.class, DvOrdinalImpl.class.newInstance());
			classesMap.put(DvParagraph.class,
					DvParagraphImpl.class.newInstance());
			classesMap.put(DvParsable.class,
					DvParsableImpl.class.newInstance());
			classesMap.put(DvPeriodicTimeSpecification.class,
					DvPeriodicTimeSpecificationImpl.class.newInstance());
			//classesMap.put(DvQuantified.class, DvQuantifiedImpl.class.newInstance());
			classesMap.put(DvState.class,
					DvStateImpl.class.newInstance());
			//classesMap.put(DvTemporal.class, DvTemporalImpl.class.newInstance());
			classesMap.put(DvText.class,
					DvTextImpl.class.newInstance());
			//classesMap.put(DvTimeSpecification.class, DvTimeSpecificationImpl.class.newInstance()); //Essa classe e abstrata.
			classesMap.put(DvURI.class,
					DvURIImpl.class.newInstance());
			//classesMap.put(ReferenceRange.class, ReferenceRangeImpl.class.newInstance());
			classesMap.put(TermMapping.class,
					TermMappingImpl.class.newInstance());
		} catch (InstantiationException ex) {
			System.out.println(ex.getMessage());
		} catch (IllegalAccessException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void main(String[] args) {
		DataValue a = new DvBoolean(true);
		DataValueRepositoryImplementation impl = new DataValueRepositoryImplementation();
		impl.save("aaa", a);
	}

}
