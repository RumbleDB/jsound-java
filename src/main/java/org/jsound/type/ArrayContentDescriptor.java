package org.jsound.type;

public class ArrayContentDescriptor {
    private TypeOrReference type;

    public ArrayContentDescriptor(TypeOrReference type) {
        this.type = type;
    }

    public TypeOrReference getType() {
        return type;
    }
}
