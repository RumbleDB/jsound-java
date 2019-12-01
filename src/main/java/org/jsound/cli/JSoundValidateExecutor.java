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

class JSoundValidateExecutor {

    private static JSoundValidateExecutor instance;

    private JSoundValidateExecutor() {
    }

    static JSoundValidateExecutor getInstance() {
        if (instance == null)
            instance = new JSoundValidateExecutor();
        return instance;
    }

    boolean validate() {
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
        Map<String, ItemType> schema = JsonParser.getRootTypeFromObject(schemaObject);
        ItemType schemaItem = schema.get(rootType);
        Item fileItem = JsonParser.getItemFromObject(fileObject);

        return fileItem.isValidAgainst(schemaItem);
    }
}
