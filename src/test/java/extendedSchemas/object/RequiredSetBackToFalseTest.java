package extendedSchemas.object;

import base.BaseTest;
import jsound.exceptions.RequiredSetBackToFalseException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class RequiredSetBackToFalseTest extends BaseTest {
    @Test(expected = RequiredSetBackToFalseException.class)
    public void requiredSetBackToFalse() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/object/requiredSetBackToFalseSchema.json",
                "targetType",
                false
        );
    }
}
