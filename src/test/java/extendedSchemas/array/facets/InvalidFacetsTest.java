package extendedSchemas.array.facets;

import base.BaseTest;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;

public class InvalidFacetsTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + "extendedSchemas/array/facets/arraySchema.json",
            "targetType",
            false
        );
    }


    @Test
    public void testInvalidValues() throws IOException {
        TypeDescriptor arrayObj = schema.get("arrayObj");
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + "array/facets/facetsError.json"));
        for (
            ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("arrays")
                .getItem()
                .getItems()
        ) {
            assertFalse(
                arrayObj.validate(itemWrapper, false)
            );
        }
    }
}
