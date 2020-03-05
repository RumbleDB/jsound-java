package extendedSchemas.object.closed;

import base.BaseTest;
import jsound.typedescriptors.object.ObjectTypeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import org.api.executors.JSoundExecutor;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertTrue;

public class ClosedFacetTest extends BaseTest {
    String filePath = "object/closed/objectFile.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/object/closed/closedSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("myObject").isObjectType());
        assertTrue(((ObjectTypeDescriptor) schema.get("myObject")).getFacets().isClosed());
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
