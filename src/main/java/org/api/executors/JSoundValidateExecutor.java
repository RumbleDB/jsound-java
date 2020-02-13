package org.api.executors;


import java.io.IOException;

public abstract class JSoundValidateExecutor extends JSoundExecutor {

    public static boolean validateFromPaths(String schemaPath, String filePath, String targetType, boolean compact)
            throws IOException {
        initApplicationFromPaths(schemaPath, filePath, targetType, compact);
        return schemaItem.validate(fileItem, false);
    }

    public static boolean validateFromFiles(String schema, String file, String targetType, boolean compact) {
        initApplicationFromFiles(schema, file, targetType, compact);
        return schemaItem.validate(fileItem, false);
    }
}
