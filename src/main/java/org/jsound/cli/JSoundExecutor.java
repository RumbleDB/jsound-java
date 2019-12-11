package org.jsound.cli;

import com.jsoniter.JsonIterator;
import jsound.exceptions.CliException;
import jsound.exceptions.ResourceNotFoundException;
import org.jsound.item.Item;
import org.jsound.json.CompactSchemaFileJsonParser;
import org.jsound.json.InstanceFileJsonParser;
import org.jsound.json.SchemaFileJsonParser;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.AtomicTypes;
import org.jsound.type.TypeDescriptor;
import org.jsound.type.TypeOrReference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.jsound.json.CompactSchemaFileJsonParser.compactSchema;

public abstract class JSoundExecutor {

    static TypeDescriptor schemaItem;
    static Item fileItem;
    private static boolean initialized = false;
    public static Map<String, TypeDescriptor> schema = new HashMap<>();
    public static JsonIterator object;

    static void initializeApplication(String schemaPath, String filePath, String rootType, boolean compact) {
        if (initialized)
            return;
        String schemaString, fileString;

        try {
            Thread.sleep(3000);
            schemaString = new String(Files.readAllBytes(Paths.get(schemaPath)));
            fileString = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (InterruptedException | IOException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

        object = JsonIterator.parse(schemaString);
        JsonIterator fileObject = JsonIterator.parse(fileString);

        for (AtomicTypes type : AtomicTypes.values()) {
            try {
                schema.put(type.getTypeName(), AtomicTypeDescriptor.buildAtomicType(type, type.getTypeName(), false));
                compactSchema.put(type.getTypeName(), new TypeOrReference(schema.get(type.getTypeName())));
            } catch (IOException e) {
                throw new CliException("Something wrong happened.");
            }
        }

        if (compact) {
            CompactSchemaFileJsonParser.createSchema();
        } else {
            SchemaFileJsonParser.createSchema();
        }

        // TODO: controllare che baseType sia valido contro il atomicTypes at hand
        // TODO: controllare che atomicItems in enumeration siano validi contro atomicTypes at hand
        // TODO: controllare che i stringtype dei field descriptors siano atomicTypes esistenti e validi
        // TODO: controllare che i default value dei field descriptors siano validi contro i loro atomicTypes
        // TODO: controllare che i stringType di arraycontent e unioncontent siano esistenti e validi

        schemaItem = schema.get(rootType);
        fileItem = InstanceFileJsonParser.getItemFromObject(fileObject);
        initialized = true;
    }
}
