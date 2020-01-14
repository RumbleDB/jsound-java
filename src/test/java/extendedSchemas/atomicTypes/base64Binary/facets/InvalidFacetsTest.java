package extendedSchemas.atomicTypes.base64Binary.facets;

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
            "extendedSchemas/atomicTypes/base64Binary/facets/base64BinarySchema.json",
            "atomicTypes/base64Binary/facets/facetsError.json",
            false
        );
    }


    @Test
    public void testInvalidValues() {
        TypeDescriptor base64BinaryObj = schema.get("base64BinaryObj");
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("base64Binaries").getItem().getItems()) {
            assertFalse(
                base64BinaryObj.validate(itemWrapper, false)
            );
        }
    }
}
