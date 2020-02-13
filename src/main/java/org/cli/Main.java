package org.cli;

import jsound.exceptions.CliException;
import jsound.exceptions.JsoundException;
import org.api.executors.JSoundAnnotateExecutor;
import org.api.executors.JSoundValidateExecutor;
import org.config.JSoundRuntimeConfiguration;

public class Main {

    public static void main(String[] args) {
        JSoundRuntimeConfiguration configuration = JSoundRuntimeConfiguration.createJSoundRuntimeConfiguration(args);
        try {
            configuration.hasNecessaryArguments();
            if (configuration.isAnnotate()) {
                if (configuration.getOutputPath() == null)
                    throw new CliException("Missing output path argument");
                try {
                    JSoundAnnotateExecutor.annotate(
                            JSoundRuntimeConfiguration.getInstance().getSchema(),
                            JSoundRuntimeConfiguration.getInstance().getFile(),
                            JSoundRuntimeConfiguration.getInstance().getTargetType(),
                            JSoundRuntimeConfiguration.getInstance().getOutputPath(),
                            JSoundRuntimeConfiguration.getInstance().isCompact()
                    );
                    System.out.println("Validation completed successfully! ✅");
                    System.out.println("Annotation completed successfully! ✅");
                } catch (JsoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                boolean isValid = JSoundValidateExecutor.validateFromPaths(
                    JSoundRuntimeConfiguration.getInstance().getSchema(),
                    JSoundRuntimeConfiguration.getInstance().getFile(),
                    JSoundRuntimeConfiguration.getInstance().getTargetType(),
                    JSoundRuntimeConfiguration.getInstance().isCompact()
                );
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
