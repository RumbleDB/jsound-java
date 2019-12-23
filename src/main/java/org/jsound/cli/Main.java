package org.jsound.cli;

import jsound.exceptions.CliException;
import jsound.exceptions.JsoundException;
import org.jsound.config.JSoundRuntimeConfiguration;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(3000);
        JSoundRuntimeConfiguration configuration = JSoundRuntimeConfiguration.createJSoundRuntimeConfiguration(args);
        try {
            configuration.hasNecessaryArguments();
            if (configuration.isValidate()) {
                boolean isValid = JSoundValidateExecutor.validate(
                    JSoundRuntimeConfiguration.getInstance().getSchema(),
                    JSoundRuntimeConfiguration.getInstance().getFile(),
                    JSoundRuntimeConfiguration.getInstance().getRootType(),
                    JSoundRuntimeConfiguration.getInstance().isCompact()
                );
                System.out.println(
                    isValid
                        ? "Validation completed successfully! ✅"
                        : "Validation failed ❌ : the file is not valid against the schema."
                );
            } else if (configuration.isAnnotate()) {
                if (configuration.getOutputPath() == null)
                    throw new CliException("Missing output path argument");
                try {
                    JSoundAnnotateExecutor.annotate(
                        JSoundRuntimeConfiguration.getInstance().getSchema(),
                        JSoundRuntimeConfiguration.getInstance().getFile(),
                        JSoundRuntimeConfiguration.getInstance().getRootType(),
                        JSoundRuntimeConfiguration.getInstance().getOutputPath(),
                        JSoundRuntimeConfiguration.getInstance().isCompact()
                    );
                    System.out.println("Validation completed successfully! ✅");
                    System.out.println("Annotation completed successfully! ✅");
                } catch (JsoundException e) {
                    System.out.println(
                        "Annotation failed ❌ : could not annotate the file with the provided the schema."
                    );
                }
            } else
                System.out.println("Please specify if you want to validate or annotate the file against the schema");
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    private static void handleException(Throwable ex) {
        if (ex != null) {
            if (ex instanceof JsoundException) {
                System.err.println("⚠️  ️" + ex.getMessage());
            } else {
                System.out.println("An error has occured: " + ex.getMessage());
                System.out.println(
                    "We should investigate this. Please contact us or file an issue on GitHub with your query."
                );
                System.out.println("Link: ");
                ex.printStackTrace();
            }
        }
    }
}
