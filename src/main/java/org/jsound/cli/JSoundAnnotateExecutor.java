package org.jsound.cli;

import jsound.exceptions.ResourceNotFoundException;
import org.jsound.config.JSoundRuntimeConfiguration;
import org.tyson.TYSONObject;

import java.io.FileWriter;
import java.io.IOException;

class JSoundAnnotateExecutor extends JSoundExecutor {

    static void annotate(String schemaPath, String filePath, String rootType, String outputPath) {
        try (FileWriter file = new FileWriter(outputPath)) {
            initializeApplication(schemaPath, filePath, rootType);
            TYSONObject rootObject = (TYSONObject) fileItem.annotate(schemaItem);
            rootObject.setTypeName(JSoundRuntimeConfiguration.getInstance().getRootType());
            file.write(rootObject.toTYSONString());
            System.out.println("Annotation completed successfully! ✅");
        } catch (IOException e) {
            throw new ResourceNotFoundException("The specified output path is not valid.");
        } catch (Exception e) {
            System.out.println("Annotation failed ❌ : could not annotate the file with the provided the schema.");
        }
    }
}
