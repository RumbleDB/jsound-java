package org.api.executors;


import java.io.IOException;

public abstract class JSoundValidateExecutor extends JSoundExecutor {

    public static boolean validate(String schemaPath, String filePath, String targetType, boolean compact)
            throws IOException {
        initializeApplication(schemaPath, filePath, targetType, compact);
        return schemaItem.validate(fileItem, false);
    }
}
