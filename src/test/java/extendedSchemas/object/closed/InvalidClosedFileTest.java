package extendedSchemas.object.closed;

import base.BaseTest;
import jsound.exceptions.ClosedNotRespectedException;
import jsound.typedescriptors.object.ObjectTypeDescriptor;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertTrue;

public class InvalidClosedFileTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/object/closed/closedSchema.json",
            "object/closed/invalidClosedFile.json",
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
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("objects").getItem().getItems()) {
            schema.get("myObject").validate(itemWrapper, false);
        }
    }

    @Test(expected = ClosedNotRespectedException.class)
    public void testValidate() {
        schemaItem.validate(fileItem, false);
    }
}
