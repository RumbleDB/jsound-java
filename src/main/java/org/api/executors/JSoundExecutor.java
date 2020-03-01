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

    public static TypeDescriptor schemaItem;
    public static Map<String, TypeDescriptor> schema;
    public static JsonIterator jsonSchemaIterator;
    static long fileLength;

    static void initApplicationFromPaths(String schemaPath, String targetType, boolean compact)
            throws IOException {
        String schemaString;
        try {
            schemaString = new String(Files.readAllBytes(Paths.get(schemaPath)));
        } catch (IOException e) {
            throw new IOException("There was an error when reading the instance or the schema file.");
        }

        JsonIterator schemaObject = JsonIterator.parse(schemaString);
        initializeSchema(schemaObject, targetType, compact);
    }

    static void initApplicationFromFiles(String readSchema, String targetType, boolean compact) {
        JsonIterator schemaObject = JsonIterator.parse(readSchema);
        initializeSchema(schemaObject, targetType, compact);
    }

    private static void initializeSchema(JsonIterator schemaObject, String targetType, boolean compact) {
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

        schemaItem = schema.getOrDefault(targetType, null);
        if (schemaItem == null)
            throw new CliException("The specified target type was not defined in the schema.");
        checkSubtypeCorrectness();
    }

    static void checkSubtypeCorrectness() {
        for (TypeDescriptor typeDescriptor : schema.values()) {
            typeDescriptor.resolveAllFacets(new HashSet<>());
            typeDescriptor.checkBaseType();
        }
    }
}
