package org.api.executors;


import com.jsoniter.JsonIterator;
import jsound.json.InstanceFileJsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class JSoundValidateExecutor extends JSoundExecutor {

    public static boolean validateFromPaths(String schemaPath, String filePath, String targetType, boolean compact)
            throws IOException {
        initApplicationFromPaths(schemaPath, targetType, compact);
        String fileString = new String(Files.readAllBytes(Paths.get(filePath)));
        return validate(fileString);
    }

    public static boolean validateFromFiles(String schema, String file, String targetType, boolean compact) {
        initApplicationFromFiles(schema, targetType, compact);
        return validate(file);
    }

    private static boolean validate(String file) {
        JsonIterator fileObject = JsonIterator.parse(file);
        fileLength = new BufferedReader(new StringReader(file)).lines().count();
        for (long i = 0; i < fileLength; i++) {
            if (!schemaItem.validate(InstanceFileJsonParser.getItemFromObject(fileObject), false))
                return false;
        }
        return true;
    }
}
