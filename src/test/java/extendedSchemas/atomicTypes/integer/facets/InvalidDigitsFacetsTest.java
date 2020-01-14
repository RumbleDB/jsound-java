package extendedSchemas.atomicTypes.integer.facets;

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

public class InvalidDigitsFacetsTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/integer/facets/integerDigitsSchema.json",
            "atomicTypes/integer/facets/digitsFacetsError.json",
            false
        );
    }


    @Test
    public void testInvalidValues() {
        TypeDescriptor integerObj = schema.get("integerObj");
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("integers").getItem().getItems()) {
            assertFalse(
                integerObj.validate(itemWrapper, false)
            );
        }
    }
}
