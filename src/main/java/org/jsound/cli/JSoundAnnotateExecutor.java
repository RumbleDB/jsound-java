package org.jsound.cli;

import jsound.exceptions.InvalidInstanceAgainstSchemaException;
import org.jsound.config.JSoundRuntimeConfiguration;
import org.tyson.TYSONObject;

import java.io.FileWriter;
import java.io.IOException;

class JSoundAnnotateExecutor extends JSoundExecutor {

    static void annotate(String schemaPath, String filePath, String rootType, String outputPath, boolean compact)
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
