package org.jsound.typedescriptors.union;

import org.jsound.typedescriptors.TypeOrReference;

import java.util.ArrayList;
import java.util.List;

public class UnionContentDescriptor {
    private List<org.jsound.typedescriptors.TypeOrReference> types;

    public UnionContentDescriptor() {
        types = new ArrayList<>();
    }

    public List<TypeOrReference> getTypes() {
        return types;
    }
}
