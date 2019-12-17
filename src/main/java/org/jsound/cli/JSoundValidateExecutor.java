package org.jsound.cli;


import java.io.IOException;

public abstract class JSoundValidateExecutor extends JSoundExecutor {

    static boolean validate(String schemaPath, String filePath, String rootType, boolean compact) throws IOException {
        initializeApplication(schemaPath, filePath, rootType, compact);
        return schemaItem.validate(fileItem, false);
    }
}
