package org.jsound.cli;

import org.jsound.config.JSoundRuntimeConfiguration;

class JSoundAnnotateExecutor {

    private static JSoundAnnotateExecutor instance;

    private JSoundAnnotateExecutor() {
    }

    static JSoundAnnotateExecutor getInstance() {
        if (instance == null)
            instance = new JSoundAnnotateExecutor();
        return instance;
    }

    void annotate() {
        String schemaPath = JSoundRuntimeConfiguration.getInstance().getSchema();
        String filePath = JSoundRuntimeConfiguration.getInstance().getFile();
        String outputpPath = JSoundRuntimeConfiguration.getInstance().getOutputPath();
    }
}
