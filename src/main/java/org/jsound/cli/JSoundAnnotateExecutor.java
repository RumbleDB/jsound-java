package org.jsound.cli;

import jsound.exceptions.JsoundException;
import org.jsound.config.JSoundRuntimeConfiguration;
import org.tyson.TYSONObject;

import java.io.FileWriter;
import java.io.IOException;

class JSoundAnnotateExecutor extends JSoundExecutor {

    private static JSoundAnnotateExecutor instance;

    private JSoundAnnotateExecutor() {
    }

    static JSoundAnnotateExecutor getInstance() {
        if (instance == null)
            instance = new JSoundAnnotateExecutor();
        return instance;
    }

    void annotate() {
        initializeApplication();
        try (FileWriter file = new FileWriter(JSoundRuntimeConfiguration.getInstance().getOutputPath())) {
            TYSONObject rootObject = (TYSONObject) fileItem.annotate(schemaItem);
            rootObject.setTypeName(JSoundRuntimeConfiguration.getInstance().getRootType());
            file.write(rootObject.toTYSONString());
        } catch (IOException e) {
            throw new JsoundException("Something bad happened");
        }
    }
}
