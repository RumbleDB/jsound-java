package org.jsound.cli;

import com.jsoniter.JsonIterator;
import jsound.exceptions.ResourceNotFoundException;
import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.config.JSoundRuntimeConfiguration;
import org.jsound.json.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public abstract class JSoundExecutor {

    private static Map<String, ItemType> schema;
    static ItemType schemaItem;
    static Item fileItem;

    void initializeApplication() {
        String schemaPath = JSoundRuntimeConfiguration.getInstance().getSchema();
        String filePath = JSoundRuntimeConfiguration.getInstance().getFile();
        String rootType = JSoundRuntimeConfiguration.getInstance().getRootType();
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
        schema = JsonParser.getRootTypeFromObject(schemaObject);
        schemaItem = schema.get(rootType);
        fileItem = JsonParser.getItemFromObject(fileObject);
    }

    public ItemType getUserDefinedItemType(String key) {
        return schema.getOrDefault(key, null);
    }
}
