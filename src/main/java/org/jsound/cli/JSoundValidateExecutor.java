package org.jsound.cli;

import org.jsound.config.JSoundRuntimeConfiguration;

class JSoundValidateExecutor {

    private static JSoundValidateExecutor instance;

    private JSoundValidateExecutor() {
    }

    static JSoundValidateExecutor getInstance() {
        if (instance == null)
            instance = new JSoundValidateExecutor();
        return instance;
    }

    void validate() {
        String schemaPath = JSoundRuntimeConfiguration.getInstance().getSchema();
        String filePath = JSoundRuntimeConfiguration.getInstance().getFile();
    }
}
