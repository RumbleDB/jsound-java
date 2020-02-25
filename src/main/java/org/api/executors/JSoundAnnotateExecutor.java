package org.api.executors;

import jsound.exceptions.InvalidInstanceAgainstSchemaException;
import jsound.tyson.TYSONObject;
import org.config.JSoundRuntimeConfiguration;

import java.io.FileWriter;
import java.io.IOException;

public class JSoundAnnotateExecutor extends JSoundExecutor {

    public static String annotate(String schemaPath, String filePath, String targetType, boolean compact) {
        validateForAnnotation(schemaPath, filePath, targetType, compact);
        TYSONObject rootObject = (TYSONObject) schemaItem.annotate(fileItem);
        rootObject.setTypeName(targetType);
        return rootObject.toTYSONString();
    }

    public static void annotate(String schemaPath, String filePath, String targetType, String outputPath, boolean compact)
            throws IOException {
        validateForAnnotation(schemaPath, filePath, targetType, compact);

        try (FileWriter file = new FileWriter(outputPath)) {
            TYSONObject rootObject = (TYSONObject) schemaItem.annotate(fileItem);
            rootObject.setTypeName(targetType);
            file.write(rootObject.toTYSONString());
        } catch (IOException e) {
            throw new IOException("The specified output path is not valid: " + outputPath);
        }
    }

    private static void validateForAnnotation(String schemaPath, String filePath, String targetType, boolean compact) {
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
}
