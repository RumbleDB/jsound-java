package extendedSchemas.atomicTypes.dateTime.facets;

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
            "extendedSchemas/atomicTypes/dateTime/facets/dateTimeSchema.json",
            "atomicTypes/dateTime/facets/facetsError.json",
            false
        );
    }


    @Test
    public void testInvalidValues() {
        TypeDescriptor dateTimeObj = schema.get("dateTimeObj");
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("dateTimes").getItem().getItems()) {
            assertFalse(
                dateTimeObj.validate(itemWrapper, false)
            );
        }
    }
}
