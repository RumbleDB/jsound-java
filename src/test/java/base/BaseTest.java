package base;

import org.api.TypeDescriptor;
import org.api.executors.JSoundExecutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jsound.json.CompactSchemaFileJsonParser.compactSchema;
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
        if (compact)
            compactSchema = new HashMap<>();
        JSoundExecutor.initializeApplication(
            schemaPathPrefix + schemaPath,
            filePathPrefix + filePath,
            "rootType",
            compact
        );
        return schema;
    }
}
