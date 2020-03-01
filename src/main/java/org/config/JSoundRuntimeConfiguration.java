package org.config;

import jsound.exceptions.CliException;

import java.util.HashMap;

public class JSoundRuntimeConfiguration {

    private static final String ARGUMENT_PREFIX = "--";
    private static final String ARGUMENT_FORMAT_ERROR_MESSAGE =
        "Invalid argument format. Required format: --property value";
    private HashMap<String, String> _arguments;
    private static JSoundRuntimeConfiguration instance;
    private final boolean annotate;

    private JSoundRuntimeConfiguration(String[] args) {
        _arguments = new HashMap<>();
        if (args[0].equals("validate"))
            annotate = false;
        else if (args[0].equals("annotate"))
            annotate = true;
        else
            throw new CliException("Please specify if you want to validate or annotate the file against the schema");
        for (int index = 1; index < args.length; index += 2) {
            if (args[index].startsWith(ARGUMENT_PREFIX))
                _arguments.put(args[index].trim().replace(ARGUMENT_PREFIX, ""), args[index + 1]);
            else
                throw new CliException(ARGUMENT_FORMAT_ERROR_MESSAGE);
        }
    }

    public static JSoundRuntimeConfiguration createJSoundRuntimeConfiguration(String[] args) {
        if (instance == null) {
            instance = new JSoundRuntimeConfiguration(args);
        }
        return instance;
    }

    public static JSoundRuntimeConfiguration getInstance() {
        return instance;
    }

    public String getOutputPath() {
        return this._arguments.getOrDefault("output", null);
    }

    public String getFile() {
        return this._arguments.getOrDefault("file", null);
    }

    public String getSchema() {
        return this._arguments.getOrDefault("schema", null);
    }

    public String getTargetType() {
        return this._arguments.getOrDefault("targetType", null);
    }

    public boolean isCompact() {
        return Boolean.parseBoolean(this._arguments.getOrDefault("compact", null));
    }

    public boolean isAnnotate() {
        return annotate;
    }

    public boolean getJSONLines() {
        return Boolean.parseBoolean(
            this._arguments.getOrDefault("JSONLines", null)
        );
    }


    public void hasNecessaryArguments() {
        if (getSchema() == null)
            throw new CliException("Missing schema argument");
        if (getFile() == null)
            throw new CliException("Missing instance file argument");
        if (getTargetType() == null)
            throw new CliException("Missing type to validate the instance file against.");
    }
}
