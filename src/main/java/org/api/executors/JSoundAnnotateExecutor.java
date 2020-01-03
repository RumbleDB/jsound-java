package org.api.executors;

import jsound.exceptions.InvalidInstanceAgainstSchemaException;
import jsound.tyson.TYSONObject;
import org.config.JSoundRuntimeConfiguration;

import java.io.FileWriter;
import java.io.IOException;

public class JSoundAnnotateExecutor extends JSoundExecutor {

    public static void annotate(String schemaPath, String filePath, String rootType, String outputPath, boolean compact)
            throws IOException {
        try {
            if (!JSoundValidateExecutor.validate(schemaPath, filePath, rootType, compact))
                throw new InvalidInstanceAgainstSchemaException(
                        "Annotation can't be done. The candidate instance is invalid against the provided schema."
                );
        } catch (Exception e) {
            throw new InvalidInstanceAgainstSchemaException(
                    "Annotation can't be done. The candidate instance is invalid against the provided schema."
            );
        }
        try (FileWriter file = new FileWriter(outputPath)) {
            TYSONObject rootObject = (TYSONObject) schemaItem.annotate(fileItem);
            rootObject.setTypeName(JSoundRuntimeConfiguration.getInstance().getRootType());
            file.write(rootObject.toTYSONString());
        } catch (IOException e) {
            throw new IOException("The specified output path is not valid: " + outputPath);
        }
    }
}
