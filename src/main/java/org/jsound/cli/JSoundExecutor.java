package org.jsound.cli;

import com.jsoniter.JsonIterator;
import jsound.exceptions.ResourceNotFoundException;
import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.json.InstanceFileJsonParser;
import org.jsound.json.CompactSchemaFileJsonParser;
import org.jsound.json.SchemaFileJsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public abstract class JSoundExecutor {

    private static Map<String, ItemType> schema;
    static ItemType schemaItem;
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
        schema = compact
            ? CompactSchemaFileJsonParser.getRootTypes(schemaObject)
            : SchemaFileJsonParser.getRootType(schemaObject);

        // TODO: controllare che baseType sia valido contro il type at hand

        schemaItem = schema.get(rootType);
        fileItem = InstanceFileJsonParser.getItemFromObject(fileObject);
        initialized = true;
    }

    public static ItemType getUserDefinedItemType(String key) {
        return schema.getOrDefault(key, null);
    }
}
