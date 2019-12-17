package org.jsound.type;

import jsound.exceptions.TypeNotResolvedException;

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
                throw new TypeNotResolvedException("Type " + stringType + " could not be resolved.");
        }
        return type;
    }

    public String getStringType() {
        return stringType;
    }

    public TypeDescriptor getType() {
        return type;
    }
}
