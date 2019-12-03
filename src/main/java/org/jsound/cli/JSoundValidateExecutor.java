package org.jsound.cli;


public class JSoundValidateExecutor extends JSoundExecutor {

    private static JSoundValidateExecutor instance;

    private JSoundValidateExecutor() {
    }

    public static JSoundValidateExecutor getInstance() {
        if (instance == null)
            instance = new JSoundValidateExecutor();
        return instance;
    }

    boolean validate() {
        initializeApplication();
        return fileItem.isValidAgainst(schemaItem);
    }
}
