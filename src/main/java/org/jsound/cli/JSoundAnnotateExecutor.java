package org.jsound.cli;

import jsound.exceptions.ResourceNotFoundException;
import org.jsound.config.JSoundRuntimeConfiguration;
import org.tyson.TYSONObject;

import java.io.FileWriter;
import java.io.IOException;

class JSoundAnnotateExecutor extends JSoundExecutor {

    static boolean annotate(String schemaPath, String filePath, String rootType, String outputPath, boolean compact) {
        try (FileWriter file = new FileWriter(outputPath)) {
            if (!JSoundValidateExecutor.validate(schemaPath, filePath, rootType, compact))
                return false;
            TYSONObject rootObject = (TYSONObject) fileItem.annotateWith(schemaItem);
            rootObject.setTypeName(JSoundRuntimeConfiguration.getInstance().getRootType());
            file.write(rootObject.toTYSONString());
        } catch (IOException e) {
            throw new ResourceNotFoundException("The specified output path is not valid.");
        } catch (Exception e) {
            System.out.println("Annotation failed ❌ : could not annotate the file with the provided the schema.");
        }
        return true;
    }
}
