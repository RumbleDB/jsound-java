package org.jsound.cli;


public abstract class JSoundValidateExecutor extends JSoundExecutor {

    static boolean validate(String schemaPath, String filePath, String rootType) {
        initializeApplication(schemaPath, filePath, rootType);
        return fileItem.isValidAgainst(schemaItem);
    }
}
