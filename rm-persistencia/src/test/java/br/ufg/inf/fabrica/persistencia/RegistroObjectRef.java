package br.ufg.inf.fabrica.persistencia;

import org.openehr.rm.support.identification.*;

/**
 * Registro que armazena instância de {@code ObjectRef}.
 */
public class RegistroObjectRef {

    public static final int TIPO_OBJECT_REF = 0;
    public static final int TIPO_PARTY_REF = 1;
    public static final int TIPO_ACCESS_GROUP_REF = 2;
    public static final int TIPO_LOCATABLE_REF = 3;

    // ObjectRef
    private String keyObjectID;
    private String namespace;
    private String type;
    private String path;
    private int tipo;

    public ObjectRef to(ObjectID objectID) {
        switch (tipo) {
            case TIPO_OBJECT_REF:
                return new ObjectRef(objectID, namespace, type);
            case TIPO_ACCESS_GROUP_REF:
                return new AccessGroupRef(objectID);
            case TIPO_PARTY_REF:
                return new PartyRef(objectID, type);
            case TIPO_LOCATABLE_REF:
                return new LocatableRef((ObjectVersionID)objectID, namespace, type, path);
            default:
                throw new RuntimeException("tipo inválido");
        }
    }
}
