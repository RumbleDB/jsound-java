package extendedSchemas.atomicTypes.dayTimeDuration.enumeration;

import base.BaseTest;
import jsound.exceptions.InvalidEnumValueException;
import org.api.ItemWrapper;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;

public class InvalidEnumInSchemaTest extends BaseTest {

    @Test(expected = InvalidEnumValueException.class)
    public void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/atomicTypes/dayTimeDuration/invalidEnumInSchema.json",
                "atomicTypes/dayTimeDuration/enumeration/dayTimeDurationEnumeration.json",
                false
        );
    }

    @Test
    public void validateEnumValues() {
        for (ItemWrapper itemWrapper : schema.get("dayTimeDurationType").getFacets().getEnumeration()) {
            assertFalse(schema.get("dayTimeDurationType").validate(itemWrapper, true));
        }
    }
}
