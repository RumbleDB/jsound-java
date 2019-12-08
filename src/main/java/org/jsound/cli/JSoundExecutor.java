package org.jsound.cli;

import com.jsoniter.JsonIterator;
import jsound.exceptions.ResourceNotFoundException;
import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.api.TypeDescriptor;
import org.jsound.json.InstanceFileJsonParser;
import org.jsound.json.CompactSchemaFileJsonParser;
import org.jsound.json.SchemaFileJsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public abstract class JSoundExecutor {

    private static Map<String, TypeDescriptor> schema;
    static TypeDescriptor schemaItem;
    static Item fileItem;
    private static boolean initialized = false;

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
        JsonIterator schemaObject = JsonIterator.parse(schemaString);
        JsonIterator fileObject = JsonIterator.parse(fileString);
        if (compact) {
            CompactSchemaFileJsonParser.object = schemaObject;
            schema = CompactSchemaFileJsonParser.getRootTypes();
        } else {
            SchemaFileJsonParser.object = schemaObject;
            schema = SchemaFileJsonParser.getSchema();
        }

        // TODO: controllare che baseType sia valido contro il type at hand
        // TODO: controllare che items in enumeration siano validi contro type at hand
        // TODO: controllare che i stringtype dei field descriptors siano type esistenti e validi
        // TODO: controllare che i default value dei field descriptors contro i loro type
        // TODO: controllare che i stringType di arraycontent e unioncontent siano esistenti e validi

        schemaItem = schema.get(rootType);
        fileItem = InstanceFileJsonParser.getItemFromObject(fileObject);
        initialized = true;
    }

    public static ItemType getUserDefinedItemType(String key) {
        return schema.getOrDefault(key, null);
    }
}
