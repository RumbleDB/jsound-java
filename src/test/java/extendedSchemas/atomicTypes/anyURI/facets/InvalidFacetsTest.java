package extendedSchemas.atomicTypes.anyURI.facets;

import base.BaseTest;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;

public class InvalidFacetsTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/anyURI/facets/anyURISchema.json",
            "atomicTypes/anyURI/facets/facetsError.json",
            false
        );
    }


    @Test
    public void testInvalidValues() {
        TypeDescriptor anyURIObj = schema.get("anyURIObj");
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("anyURIs").getItem().getItems()) {
            assertFalse(
                anyURIObj.validate(itemWrapper, false)
            );
        }
    }
}
