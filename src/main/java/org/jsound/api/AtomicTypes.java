package org.jsound.api;

import com.jsoniter.JsonIterator;
import org.jsound.facets.FacetTypes;
import org.jsound.type.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.*;
import static org.jsound.json.SchemaFileJsonParser.*;

public enum AtomicTypes {
    STRING(new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH))) {
        @Override
        public TypeDescriptor buildAtomicType(String name, JsonIterator object) throws IOException {
            return new TypeDescriptor(
                    TypeFactory.getInstance()
                        .createStringType(name, createFacets(this._allowedFacets, object))
            );
        }
    },
    INTEGER(
            new HashSet<>(
                    Arrays.asList(
                        MIN_INCLUSIVE,
                        MAX_INCLUSIVE,
                        MIN_EXCLUSIVE,
                        MAX_EXCLUSIVE,
                        TOTAL_DIGITS,
                        FRACTION_DIGITS
                    )
            )
    ) {
        @Override
        public TypeDescriptor buildAtomicType(String name, JsonIterator object) throws IOException {
            return new TypeDescriptor(
                    TypeFactory.getInstance()
                        .createIntegerType(name, createFacets(this._allowedFacets, object))
            );
        }
    },
    DECIMAL(
            new HashSet<>(
                    Arrays.asList(
                        MIN_INCLUSIVE,
                        MAX_INCLUSIVE,
                        MIN_EXCLUSIVE,
                        MAX_EXCLUSIVE,
                        TOTAL_DIGITS,
                        FRACTION_DIGITS
                    )
            )
    ) {
        @Override
        public TypeDescriptor buildAtomicType(String name, JsonIterator object) throws IOException {
            return new TypeDescriptor(
                    TypeFactory.getInstance()
                        .createDecimalType(name, createFacets(this._allowedFacets, object))
            );
        }
    },
    DOUBLE(new HashSet<>(Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE))) {
        @Override
        public TypeDescriptor buildAtomicType(String name, JsonIterator object) throws IOException {
            return new TypeDescriptor(
                    TypeFactory.getInstance()
                        .createDoubleType(name, createFacets(this._allowedFacets, object))
            );
        }
    },
    HEXBINARY(new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH))) {
        @Override
        public TypeDescriptor buildAtomicType(String name, JsonIterator object) throws IOException {
            return new TypeDescriptor(
                    TypeFactory.getInstance()
                        .createHexBinaryType(name, createFacets(this._allowedFacets, object))
            );
        }
    },
    BASE64BINARY(new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH))) {
        @Override
        public TypeDescriptor buildAtomicType(String name, JsonIterator object) throws IOException {
            return new TypeDescriptor(
                    TypeFactory.getInstance()
                        .createBase64BinaryType(name, createFacets(this._allowedFacets, object))
            );
        }
    },
    ANYURI(new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH))) {
        @Override
        public TypeDescriptor buildAtomicType(String name, JsonIterator object) throws IOException {
            return new TypeDescriptor(
                    TypeFactory.getInstance()
                        .createAnyURIType(name, createFacets(this._allowedFacets, object))
            );
        }
    };

    final Set<FacetTypes> _allowedFacets;

    AtomicTypes(Set<FacetTypes> allowedFacets) {
        this._allowedFacets = allowedFacets;
    }

    public abstract TypeDescriptor buildAtomicType(String name, JsonIterator object) throws IOException;
}
