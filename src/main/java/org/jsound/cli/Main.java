package org.jsound.cli;

import jsound.exceptions.CliException;
import jsound.exceptions.JsoundException;
import org.jsound.config.JSoundRuntimeConfiguration;

public class Main {

    public static void main(String[] args) {
        JSoundRuntimeConfiguration configuration = JSoundRuntimeConfiguration.createJSoundRuntimeConfiguration(args);
        try {
            if (configuration.getSchema() == null)
                throw new CliException("Missing schema argument");
            if (configuration.getFile() == null)
                throw new CliException("Missing JSON file argument");
            if (configuration.getRootType() == null)
                throw new CliException("Missing root type argument");
            if (configuration.isValidate()) {
                System.out.println(JSoundValidateExecutor.getInstance().validate());
            } else if (configuration.isAnnotate()) {
                if (configuration.getOutputPath() == null)
                    throw new CliException("Missing output path argument");
                JSoundAnnotateExecutor.getInstance().annotate();
            } else {
                System.out.println("Please specify if you want to validate or annotate the file against the schema");
            }
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
                    "We should investigate this 🙈. Please contact us or file an issue on GitHub with your query."
                );
                System.out.println("Link: ");
                ex.printStackTrace();
            }
        }
    }
}
