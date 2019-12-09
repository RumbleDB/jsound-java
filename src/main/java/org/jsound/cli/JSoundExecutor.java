package org.jsound.cli;

import com.jsoniter.JsonIterator;
import jsound.exceptions.CliException;
import jsound.exceptions.ResourceNotFoundException;
import org.jsound.api.AtomicTypeDescriptor;
import org.jsound.api.AtomicTypes;
import org.jsound.api.Item;
import org.jsound.api.TypeDescriptor;
import org.jsound.json.CompactSchemaFileJsonParser;
import org.jsound.json.InstanceFileJsonParser;
import org.jsound.json.SchemaFileJsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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
            } catch (IOException e) {
                throw new CliException("Something wrong happened.");
            }
        }

        if (compact) {
            CompactSchemaFileJsonParser.createSchema();
        } else {
            SchemaFileJsonParser.createSchema();
        }

        // TODO: controllare che baseType sia valido contro il type at hand
        // TODO: controllare che items in enumeration siano validi contro type at hand
        // TODO: controllare che i stringtype dei field descriptors siano type esistenti e validi
        // TODO: controllare che i default value dei field descriptors siano validi contro i loro type
        // TODO: controllare che i stringType di arraycontent e unioncontent siano esistenti e validi

        schemaItem = schema.get(rootType);
        fileItem = InstanceFileJsonParser.getItemFromObject(fileObject);
        initialized = true;
    }
}
