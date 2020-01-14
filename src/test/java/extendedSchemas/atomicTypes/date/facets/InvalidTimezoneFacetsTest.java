package extendedSchemas.atomicTypes.date.facets;

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

public class InvalidTimezoneFacetsTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/date/facets/dateTimezoneSchema.json",
            "atomicTypes/date/facets/timezoneFacetsError.json",
            false
        );
    }


    @Test
    public void testInvalidValues() {
        TypeDescriptor dateObj = schema.get("dateObj");
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("dates").getItem().getItems()) {
            assertFalse(
                dateObj.validate(itemWrapper, false)
            );
        }
    }
}
