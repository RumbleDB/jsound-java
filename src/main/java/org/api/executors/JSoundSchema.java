package org.api.executors;

import com.jsoniter.JsonIterator;
import jsound.exceptions.InvalidInstanceAgainstSchemaException;
import jsound.exceptions.JsoundException;
import jsound.json.InstanceFileJsonParser;
import org.api.TypeDescriptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSoundSchema {
    private TypeDescriptor schemaItem;
    private String instanceFile;

    public JSoundSchema(TypeDescriptor schemaItem) {
        this.schemaItem = schemaItem;
    }

    public boolean validateInstance(String instance) {
        return schemaItem.validate(InstanceFileJsonParser.getItemFromObject(JsonIterator.parse(instance)), false);
    }

    public boolean validateJSONFromPath(String filePath) throws IOException {
        instanceFile = new String(Files.readAllBytes(Paths.get(filePath)));
        return validateInstance(instanceFile);
    }

    public boolean validateJSONLinesFromPath(String filePath) throws IOException {
        BufferedReader instanceFileReader = Files.newBufferedReader(Paths.get(filePath));
        String line;
        while((line = instanceFileReader.readLine()) != null) {
            if (!validateInstance(line))
                return false;
        }
        return true;
    }


    public String annotateInstance(String instance) {
        validateInstance(instance);
        return schemaItem.annotate(InstanceFileJsonParser.getItemFromObject(JsonIterator.parse(instance)))
            .toTYSONString();
    }

    public void annotateJSONFromPath(String filePath, String outputPath) {
        try {
            if (!validateJSONFromPath(filePath))
                throw new InvalidInstanceAgainstSchemaException(
                        "Annotation can't be done. The candidate instance is invalid against the provided schema."
                );
        } catch (Exception e) {
            throw new InvalidInstanceAgainstSchemaException(
                    "Annotation can't be done. The candidate instance is invalid against the provided schema."
            );
        }
        try (FileWriter file = new FileWriter(outputPath)) {
            file.write(annotateInstance(instanceFile));
        } catch (IOException e) {
            throw new JsoundException("The specified output file path is not valid: " + outputPath);
        }
    }

    public void annotateJSONLinesFromPath(String filePath, String outputPath) {
        try {
            if (!validateJSONLinesFromPath(filePath))
                throw new InvalidInstanceAgainstSchemaException(
                        "Annotation can't be done. The candidate instance is invalid against the provided schema."
                );
        } catch (Exception e) {
            throw new InvalidInstanceAgainstSchemaException(
                    "Annotation can't be done. The candidate instance is invalid against the provided schema."
            );
        }
        try (FileWriter file = new FileWriter(outputPath)) {
            BufferedReader instanceFileReader = Files.newBufferedReader(Paths.get(filePath));
            String line;
            while((line = instanceFileReader.readLine()) != null) {
                file.append(schemaItem.annotate(InstanceFileJsonParser.getItemFromObject(JsonIterator.parse(line))).toTYSONString())
                        .append("\n");
            }
        } catch (IOException e) {
            throw new JsoundException("The specified output file path is not valid: " + outputPath);
        }
    }
}
