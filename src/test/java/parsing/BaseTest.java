package parsing;

import org.api.executors.JSoundExecutor;

import java.io.IOException;
import java.util.HashMap;

import static jsound.json.CompactSchemaFileJsonParser.compactSchema;
import static org.api.executors.JSoundExecutor.schema;

public class BaseTest {

    protected static void initializeApplication(String schemaPath, String filePath, String rootType, boolean compact)
            throws IOException {
        schema = new HashMap<>();
        if (compact)
            compactSchema = new HashMap<>();
        JSoundExecutor.initializeApplication(schemaPath, filePath, rootType, compact);
    }
}
