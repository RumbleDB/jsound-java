package extendedSchemas.atomicTypes.integer.enumeration;

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
                "extendedSchemas/atomicTypes/integer/invalidEnumInSchema.json",
                "atomicTypes/integer/enumeration/integerEnumeration.json",
                false
        );
    }

    @Test
    public void validateEnumValues() {
        for (ItemWrapper itemWrapper : schema.get("integerType").getFacets().getEnumeration()) {
            assertFalse(schema.get("integerType").validate(itemWrapper, true));
        }
    }
}
