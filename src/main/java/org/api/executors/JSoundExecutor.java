package org.api.executors;

import com.jsoniter.JsonIterator;
import jsound.exceptions.CliException;
import org.api.Item;
import jsound.json.CompactSchemaFileJsonParser;
import jsound.json.InstanceFileJsonParser;
import jsound.json.SchemaFileJsonParser;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.AtomicTypes;
import org.api.TypeDescriptor;
import jsound.typedescriptors.TypeOrReference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static jsound.json.CompactSchemaFileJsonParser.compactSchema;

public abstract class JSoundExecutor {

    public static TypeDescriptor schemaItem;
    public static Item fileItem;
    public static Map<String, TypeDescriptor> schema = new HashMap<>();
    public static JsonIterator jsonSchemaIterator;

    public static void initializeApplication(String schemaPath, String filePath, String rootType, boolean compact)
            throws IOException {
        String schemaString, fileString;

        try {
            schemaString = new String(Files.readAllBytes(Paths.get(schemaPath)));
            fileString = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new IOException("There was an error when reading the instance or the schema file.");
        }

        jsonSchemaIterator = JsonIterator.parse(schemaString);
        JsonIterator fileObject = JsonIterator.parse(fileString);

        for (AtomicTypes type : AtomicTypes.values()) {
            try {
                schema.put(type.getTypeName(), AtomicTypeDescriptor.buildAtomicType(type, type.getTypeName(), false));
                compactSchema.put(type.getTypeName(), new TypeOrReference(schema.get(type.getTypeName())));
            } catch (IOException e) {
                throw new CliException("Something wrong happened on our end.");
            }
        }

        if (compact)
            CompactSchemaFileJsonParser.createSchema();
        else
            SchemaFileJsonParser.createSchema();

        schemaItem = schema.getOrDefault(rootType, null);
        if (schemaItem == null)
            throw new CliException("The specified root type was not defined in the schema.");
        fileItem = InstanceFileJsonParser.getItemFromObject(fileObject);
    }
}
