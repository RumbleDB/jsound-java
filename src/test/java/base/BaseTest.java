package base;

import org.api.TypeDescriptor;
import org.api.executors.JSoundExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static jsound.json.CompactSchemaFileJsonParser.compactSchema;
import static jsound.json.SchemaFileJsonParser.shouldCheckBaseType;
import static org.api.executors.JSoundExecutor.schema;

public class BaseTest {
    private static final String filePathPrefix = "src/main/resources/instanceFiles/";
    private static final String schemaPathPrefix = "src/main/resources/";

    protected static Map<String, TypeDescriptor> initializeApplication(
            String schemaPath,
            String filePath,
            boolean compact
    )
            throws IOException {
        schema = new HashMap<>();
        shouldCheckBaseType = new ArrayList<>();
        if (compact)
            compactSchema = new HashMap<>();
        JSoundExecutor.initApplicationFromPaths(
            schemaPathPrefix + schemaPath,
            filePathPrefix + filePath,
            "targetType",
            compact
        );
        return schema;
    }
}
