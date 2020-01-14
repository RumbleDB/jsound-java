package extendedSchemas.atomicTypes.hexBinary.facets;

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
            "extendedSchemas/atomicTypes/hexBinary/facets/hexBinarySchema.json",
            "atomicTypes/hexBinary/facets/facetsError.json",
            false
        );
    }


    @Test
    public void testInvalidValues() {
        TypeDescriptor hexBinaryObj = schema.get("hexBinaryObj");
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("hexBinaries").getItem().getItems()) {
            assertFalse(
                hexBinaryObj.validate(itemWrapper, false)
            );
        }
    }
}
