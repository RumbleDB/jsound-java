package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;

import static org.jsound.cli.JSoundExecutor.schema;

public class TypeOrReference {

    private TypeDescriptor type;
    private String stringType;

    public TypeOrReference(TypeDescriptor type) {
        this.type = type;
    }

    public TypeOrReference(String stringType) {
        this.stringType = stringType;
    }

    public TypeDescriptor getTypeDescriptor() {
        if (type == null) {
            type = schema.getOrDefault(stringType, null);
            if (type == null)
                throw new InvalidSchemaException("Type " + stringType + " does not exist.");
        }
        return type;
    }

    public String getStringType() {
        return stringType;
    }
}
