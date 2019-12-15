package org.jsound.cli;


public abstract class JSoundValidateExecutor extends JSoundExecutor {

    static boolean validate(String schemaPath, String filePath, String rootType, boolean compact) {
        initializeApplication(schemaPath, filePath, rootType, compact);
        return schemaItem.validate(fileItem);
    }
}
