package extendedSchemas.object.closed;

import base.BaseTest;
import jsound.typedescriptors.object.ObjectTypeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertTrue;

public class ClosedFacetTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/object/closed/closedSchema.json",
            "object/closed/objectFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("myObject").isObjectType());
        assertTrue(((ObjectTypeDescriptor) schema.get("myObject")).getFacets().isClosed());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
