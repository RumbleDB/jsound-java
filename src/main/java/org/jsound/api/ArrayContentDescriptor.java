package org.jsound.api;

public class ArrayContentDescriptor {
    private TypeDescriptor type;
    private String stringType;

    public ArrayContentDescriptor(TypeDescriptor type) {
        this.type = type;
    }

    public ArrayContentDescriptor(String stringType) {
        this.stringType = stringType;
    }

    public TypeDescriptor getType() {
        return type;
    }
}
