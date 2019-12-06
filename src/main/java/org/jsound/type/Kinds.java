package org.jsound.type;

import com.jsoniter.JsonIterator;
import org.jsound.api.TypeDescriptor;

import java.io.IOException;

import static org.jsound.json.SchemaFileJsonParser.*;

public enum Kinds {
    ATOMIC {
        @Override
        public TypeDescriptor buildTypeDescriptor(String name, JsonIterator object) throws IOException {
            return buildAtomicTypeDescriptor(name, object);
        }
    },
    OBJECT {
        @Override
        public TypeDescriptor buildTypeDescriptor(String name, JsonIterator object) throws IOException {
            return buildObjectTypeDescriptor(name, object);
        }
    },
    ARRAY {
        @Override
        public TypeDescriptor buildTypeDescriptor(String name, JsonIterator object) throws IOException {
            return buildArrayTypeDescriptor(name, object);
        }
    },
    UNION {
        @Override
        public TypeDescriptor buildTypeDescriptor(String name, JsonIterator object) throws IOException {
            return buildUnionTypeDescriptor(name, object);
        }
    };

    public abstract TypeDescriptor buildTypeDescriptor(String name, JsonIterator object) throws IOException;
}
