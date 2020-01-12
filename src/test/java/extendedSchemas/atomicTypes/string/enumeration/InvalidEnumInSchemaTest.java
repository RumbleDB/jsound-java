package extendedSchemas.atomicTypes.string.enumeration;

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
            "extendedSchemas/atomicTypes/string/invalidEnumInSchema.json",
            "atomicTypes/string/enumeration/stringEnumeration.json",
            false
        );
    }

    @Test
    public void validateEnumValues() {
        for (ItemWrapper itemWrapper : schema.get("stringType").getFacets().getEnumeration()) {
            assertFalse(schema.get("stringType").validate(itemWrapper, true));
        }
    }
}
