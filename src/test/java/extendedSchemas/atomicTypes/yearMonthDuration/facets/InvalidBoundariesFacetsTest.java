package extendedSchemas.atomicTypes.yearMonthDuration.facets;

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

public class InvalidBoundariesFacetsTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/yearMonthDuration/facets/yearMonthDurationBoundariesSchema.json",
            "atomicTypes/yearMonthDuration/facets/boundariesFacetsError.json",
            false
        );
    }


    @Test
    public void testInvalidValues() {
        TypeDescriptor yearMonthDurationObj = schema.get("yearMonthDurationObj");
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("yearMonthDurations").getItem().getItems()) {
            if (
                yearMonthDurationObj.validate(itemWrapper, false)
            )
                System.out.println("hello");
        }
    }
}
