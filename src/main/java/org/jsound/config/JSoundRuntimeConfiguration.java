package org.jsound.config;

import jsound.exceptions.CliException;

import java.util.HashMap;

public class JSoundRuntimeConfiguration {

    private static final String ARGUMENT_PREFIX = "--";
    private static final String ARGUMENT_FORMAT_ERROR_MESSAGE =
        "Invalid argument format. Required format: --property value";
    private HashMap<String, String> _arguments;
    private static JSoundRuntimeConfiguration instance;

    private JSoundRuntimeConfiguration(String[] args) {
        _arguments = new HashMap<>();
        for (int index = 0; index < args.length; index += 2) {
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

    public String getConfigurationArgument(String key) {
        return this._arguments.getOrDefault(key, null);
    }

    public String getOutputPath() {
        return this._arguments.getOrDefault("output-path", null);
    }

    public boolean getOverwrite() {
        if (this._arguments.containsKey("overwrite"))
            return this._arguments.get("overwrite").equals("yes");
        else
            return false;
    }

    public String getLogPath() {
        return this._arguments.getOrDefault("log-path", null);
    }

    public String getFile() {
        return this._arguments.getOrDefault("file", null);
    }

    public String getSchema() {
        return this._arguments.getOrDefault("schema", null);
    }

    public String getRootType() {
        return this._arguments.getOrDefault("root", null);
    }

    public boolean isValidate() {
        if (this._arguments.containsKey("validate"))
            return this._arguments.get("validate").equals("yes");
        else
            return false;
    }

    public boolean isAnnotate() {
        if (this._arguments.containsKey("annotate"))
            return this._arguments.get("annotate").equals("yes");
        else
            return false;
    }
}
