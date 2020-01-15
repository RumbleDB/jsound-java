package extendedSchemas.atomicTypes.dayTimeDuration.facets;

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
            "extendedSchemas/atomicTypes/dayTimeDuration/facets/dayTimeDurationBoundariesSchema.json",
            "atomicTypes/dayTimeDuration/facets/boundariesFacetsError.json",
            false
        );
    }


    @Test
    public void testInvalidValues() {
        TypeDescriptor dayTimeDurationObj = schema.get("dayTimeDurationObj");
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("dayTimeDurations").getItem().getItems()) {
            if (
                dayTimeDurationObj.validate(itemWrapper, false)
            )
                System.out.println("hello");
        }
    }
}
