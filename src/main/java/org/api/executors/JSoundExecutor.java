package org.api.executors;

import com.jsoniter.JsonIterator;
import jsound.exceptions.CliException;
import jsound.json.CompactSchemaFileJsonParser;
import jsound.json.InstanceFileJsonParser;
import jsound.json.SchemaFileJsonParser;
import jsound.typedescriptors.TypeOrReference;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.AtomicTypes;
import org.api.ItemWrapper;
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
    public static ItemWrapper fileItem;
    public static Map<String, TypeDescriptor> schema;
    public static JsonIterator jsonSchemaIterator;

    public static void initApplicationFromPaths(String schemaPath, String filePath, String targetType, boolean compact)
            throws IOException {
        String schemaString, fileString;

        try {
            schemaString = new String(Files.readAllBytes(Paths.get(schemaPath)));
            fileString = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new IOException("There was an error when reading the instance or the schema file.");
        }

        JsonIterator schemaObject = JsonIterator.parse(schemaString);
        JsonIterator fileObject = JsonIterator.parse(fileString);
        initializeSchema(schemaObject, fileObject, targetType, compact);
    }

    public static void initApplicationFromFiles(String readSchema, String readFile, String targetType, boolean compact) {
        JsonIterator schemaObject = JsonIterator.parse(readSchema);
        JsonIterator fileObject = JsonIterator.parse(readFile);
        initializeSchema(schemaObject, fileObject, targetType, compact);
    }

    public static void initializeSchema(JsonIterator schemaObject, JsonIterator fileObject, String targetType, boolean compact) {
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
        fileItem = InstanceFileJsonParser.getItemFromObject(fileObject);
        checkSubtypeCorrectness();
    }

    static void checkSubtypeCorrectness() {
        for (TypeDescriptor typeDescriptor : schema.values()) {
            typeDescriptor.resolveAllFacets(new HashSet<>());
            typeDescriptor.checkBaseType();
        }
    }
}
