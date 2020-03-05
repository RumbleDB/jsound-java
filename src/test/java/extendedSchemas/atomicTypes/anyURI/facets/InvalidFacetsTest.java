package extendedSchemas.atomicTypes.anyURI.facets;

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
    String filePath = "atomicTypes/anyURI/facets/facetsError.json";
    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/anyURI/facets/anyURISchema.json",
                "targetType",
                false
        );
    }


    @Test
    public void testInvalidValues() throws IOException {
        TypeDescriptor anyURIObj = schema.get("anyURIObj");
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("anyURIs").getItem().getItems()) {
            assertFalse(
                anyURIObj.validate(itemWrapper, false)
            );
        }
    }
}
