package org.jsound.typedescriptors.array;

import org.jsound.typedescriptors.TypeOrReference;

public class ArrayContentDescriptor {
    private TypeOrReference type;

    public ArrayContentDescriptor(TypeOrReference type) {
        this.type = type;
    }

    public TypeOrReference getType() {
        return type;
    }
}
