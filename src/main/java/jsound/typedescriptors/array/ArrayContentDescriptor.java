package jsound.typedescriptors.array;

import jsound.typedescriptors.TypeOrReference;

public class ArrayContentDescriptor {
    private TypeOrReference type;

    public ArrayContentDescriptor(TypeOrReference type) {
        this.type = type;
    }

    public TypeOrReference getType() {
        return type;
    }
}
