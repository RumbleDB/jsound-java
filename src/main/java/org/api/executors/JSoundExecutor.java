package org.api.executors;

import com.jsoniter.JsonIterator;
import jsound.exceptions.CliException;
import jsound.json.CompactSchemaFileJsonParser;
import jsound.json.SchemaFileJsonParser;
import jsound.typedescriptors.TypeOrReference;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.AtomicTypes;
import org.api.TypeDescriptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static jsound.json.CompactSchemaFileJsonParser.compactSchema;

public abstract class JSoundExecutor {

    public static Map<String, TypeDescriptor> schema;
    public static JsonIterator jsonSchemaIterator;

    private static String parseFile(String filePath) throws IOException {
        String file;
        try {
            file = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new IOException("There was an error when reading the instance or the schema file.");
        }
        return file;
    }

    public static JSoundSchema loadSchemaFromPath(String schemaPath, String targetType, boolean compact)
            throws IOException {
        return loadSchema(parseFile(schemaPath), targetType, compact);
    }

    public static JSoundSchema loadSchema(String schema, String targetType, boolean compact) {
        return new JSoundSchema(initializeSchema(JsonIterator.parse(schema), targetType, compact));
    }

    private static TypeDescriptor initializeSchema(JsonIterator schemaObject, String targetType, boolean compact) {
        compactSchema = new HashMap<>();
        schema = new HashMap<>();
        jsonSchemaIterator = schemaObject;
        for (AtomicTypes type : AtomicTypes.values()) {
            try {
                schema.put(type.getTypeName(), AtomicTypeDescriptor.buildAtomicType(type, type.getTypeName(), false));
                if (compact)
                    compactSchema.put(type.getTypeName(), new TypeOrReference(schema.get(type.getTypeName())));
            } catch (IOException e) {
                throw new CliException("Something wrong happened on our end.");
            }
        }

        if (compact)
            CompactSchemaFileJsonParser.createSchema();
        else
            SchemaFileJsonParser.createSchema();

        TypeDescriptor schemaItem = schema.getOrDefault(targetType, null);
        if (schemaItem == null)
            throw new CliException("The specified target type was not defined in the schema.");
        checkSubtypeCorrectness();
        return schemaItem;
    }

    static void checkSubtypeCorrectness() {
        for (TypeDescriptor typeDescriptor : schema.values()) {
            typeDescriptor.resolveAllFacets(new HashSet<>());
            typeDescriptor.checkBaseType();
        }
    }
}
