package org.cli;

import jsound.exceptions.CliException;
import jsound.exceptions.JsoundException;
import org.api.executors.JSoundExecutor;
import org.api.executors.JSoundSchema;
import org.config.JSoundRuntimeConfiguration;

public class Main {

    public static void main(String[] args) {
        JSoundRuntimeConfiguration configuration = JSoundRuntimeConfiguration.createJSoundRuntimeConfiguration(args);
        try {
            configuration.hasNecessaryArguments();
            JSoundSchema schema = JSoundExecutor.loadSchemaFromPath(
                JSoundRuntimeConfiguration.getInstance().getSchema(),
                JSoundRuntimeConfiguration.getInstance().getTargetType(),
                JSoundRuntimeConfiguration.getInstance().isCompact()
            );
            if (configuration.isAnnotate()) {
                if (configuration.getOutputPath() == null)
                    throw new CliException("Missing output path argument");
                try {
                    if (configuration.getJSONLine())
                        schema.annotateJSONLineFromPath(
                                JSoundRuntimeConfiguration.getInstance().getFile(),
                                JSoundRuntimeConfiguration.getInstance().getOutputPath()
                        );
                    else
                        schema.annotateJSONFromPath(
                            JSoundRuntimeConfiguration.getInstance().getFile(),
                            JSoundRuntimeConfiguration.getInstance().getOutputPath()
                        );
                    System.out.println("Validation completed successfully! ✅");
                    System.out.println("Annotation completed successfully! ✅");
                } catch (JsoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                boolean isValid;
                if (configuration.getJSONLine())
                    isValid = schema.validateJSONLineFromPath(JSoundRuntimeConfiguration.getInstance().getFile());
                else
                    isValid = schema.validateJSONFromPath(JSoundRuntimeConfiguration.getInstance().getFile());
                System.out.println(
                    isValid
                        ? "Validation completed successfully! ✅"
                        : "Validation failed ❌ : the file is not valid against the schema."
                );
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    private static void handleException(Throwable ex) {
        if (ex != null) {
            if (ex instanceof JsoundException) {
                System.err.println("⚠️ ️ " + ex.getMessage());
            } else {
                System.out.println("An error has occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
