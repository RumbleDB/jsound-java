package extendedSchemas.object.closed;

import base.BaseTest;
import jsound.exceptions.ClosedNotRespectedException;
import jsound.typedescriptors.object.ObjectTypeDescriptor;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import org.api.executors.JSoundExecutor;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertTrue;

public class InvalidClosedFileTest extends BaseTest {
    String filePath = "object/closed/invalidClosedFile.json";

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

    @Test(expected = ClosedNotRespectedException.class)
    public void testValidateSingularly() {
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("objects").getItem().getItems()) {
            schema.get("myObject").validate(itemWrapper, false);
        }
    }

    @Test(expected = ClosedNotRespectedException.class)
    public void testValidate() throws IOException {
        jSoundSchema.validateJSONFromPath(filePathPrefix + filePath);
    }
}
