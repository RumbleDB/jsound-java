package org.api.executors;

import com.jsoniter.JsonIterator;
import jsound.exceptions.InvalidInstanceAgainstSchemaException;
import jsound.exceptions.JsoundException;
import jsound.json.InstanceFileJsonParser;
import jsound.tyson.TYSONObject;
import org.config.JSoundRuntimeConfiguration;
import org.graalvm.compiler.api.replacements.Snippet;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSoundAnnotateExecutor extends JSoundExecutor {

    public static String annotateFromPaths(String schemaPath, String filePath, String targetType, boolean compact) {
        validateFromPaths(schemaPath, filePath, targetType, compact);
        try {
            return getAnnotation(new String(Files.readAllBytes(Paths.get(filePath))));
        } catch (IOException e) {
            throw new JsoundException("The specified input file path is not valid: " + filePath);
        }

    }

    public static void annotateFromPaths(
            String schemaPath,
            String filePath,
            String targetType,
            String outputPath,
            boolean compact
    ) {
        validateFromPaths(schemaPath, filePath, targetType, compact);
        try (FileWriter file = new FileWriter(outputPath)) {
            file.write(getAnnotation(new String(Files.readAllBytes(Paths.get(filePath)))));
        } catch (IOException e) {
            throw new JsoundException("The specified input file path or output file path is not valid: " + outputPath);
        }
    }

    public static String annotateFromFiles(String schema, String candidateInstance, String targetType, boolean compact) {
        validateFromFiles(schema, candidateInstance, targetType, compact);
        return getAnnotation(candidateInstance);
    }

    private static void validateFromPaths(String schemaPath, String filePath, String targetType, boolean compact) {
        try {
            if (!JSoundValidateExecutor.validateFromPaths(schemaPath, filePath, targetType, compact))
                throw new InvalidInstanceAgainstSchemaException(
                        "Annotation can't be done. The candidate instance is invalid against the provided schema."
                );
        } catch (Exception e) {
            throw new InvalidInstanceAgainstSchemaException(
                    "Annotation can't be done. The candidate instance is invalid against the provided schema."
            );
        }
    }

    private static void validateFromFiles(String schema, String file, String targetType, boolean compact) {
        try {
            if (!JSoundValidateExecutor.validateFromFiles(schema, file, targetType, compact))
                throw new InvalidInstanceAgainstSchemaException(
                        "Annotation can't be done. The candidate instance is invalid against the provided schema."
                );
        } catch (Exception e) {
            throw new InvalidInstanceAgainstSchemaException(
                    "Annotation can't be done. The candidate instance is invalid against the provided schema."
            );
        }
    }

    private static String getAnnotation(String file) {
        StringBuilder sb = new StringBuilder();
        JsonIterator fileObject = JsonIterator.parse(file);
        for (long i = 0; i < fileLength; i++)
            sb.append(schemaItem.annotate(InstanceFileJsonParser.getItemFromObject(fileObject)).toTYSONString())
                    .append("\n");
        return sb.toString();
    }
}
