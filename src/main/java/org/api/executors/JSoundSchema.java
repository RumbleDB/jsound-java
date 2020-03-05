package org.api.executors;

import com.jsoniter.JsonIterator;
import jsound.exceptions.InvalidInstanceAgainstSchemaException;
import jsound.exceptions.JsoundException;
import jsound.json.InstanceFileJsonParser;
import jsound.tyson.TYSONItem;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSoundSchema {
    public ItemWrapper instanceItem;
    private TypeDescriptor schemaItem;

    public JSoundSchema(TypeDescriptor schemaItem) {
        this.schemaItem = schemaItem;
    }

    public boolean validateInstance(String instance) {
        return schemaItem.validate(InstanceFileJsonParser.getItemFromObject(JsonIterator.parse(instance)), false);
    }

    public boolean validateJSONFromPath(String filePath) throws IOException {
        instanceItem = InstanceFileJsonParser.getItemFromObject(
            JsonIterator.parse(new String(Files.readAllBytes(Paths.get(filePath))))
        );
        return schemaItem.validate(instanceItem, false);
    }

    public boolean validateJSONLinesFromPath(String filePath) throws IOException {
        BufferedReader instanceFileReader = Files.newBufferedReader(Paths.get(filePath));
        String line;
        while ((line = instanceFileReader.readLine()) != null) {
            if (!validateInstance(line))
                return false;
        }
        return true;
    }

    private boolean validateInstanceForAnnotation(String instance) {
        instanceItem = InstanceFileJsonParser.getItemFromObject(JsonIterator.parse(instance));
        return schemaItem.validate(instanceItem, false);
    }

    public String annotateInstance(String instance) {
        if (!validateInstanceForAnnotation(instance))
            throw new InvalidInstanceAgainstSchemaException(
                    "Annotation can't be done. The candidate instance is invalid against the provided schema."
            );
        return schemaItem.annotate(instanceItem).toTYSONString();
    }

    public TYSONItem annotateJSONFromPath(String filePath) throws IOException {
        if (!validateInstanceForAnnotation(new String(Files.readAllBytes(Paths.get(filePath)))))
            throw new InvalidInstanceAgainstSchemaException(
                    "Annotation can't be done. The candidate instance is invalid against the provided schema."
            );
        return schemaItem.annotate(instanceItem);
    }

    public void annotateJSONLinesFromPath(String filePath, String outputPath) {
        try (FileWriter file = new FileWriter(outputPath)) {
            BufferedReader instanceFileReader = Files.newBufferedReader(Paths.get(filePath));
            String line;
            while ((line = instanceFileReader.readLine()) != null) {
                file.append(annotateInstance(line)).append("\n");
            }
        } catch (IOException e) {
            throw new JsoundException("The specified output file path is not valid: " + outputPath);
        }
    }
}
