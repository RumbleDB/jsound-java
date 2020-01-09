package extendedSchemas.atomicTypes.base64Binary.enumeration;

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
                "extendedSchemas/atomicTypes/base64Binary/invalidEnumInSchema.json",
                "atomicTypes/base64Binary/enumeration/base64BinaryEnumeration.json",
                false
        );
    }
    @Test
    public void validateEnumValues() {
        for (ItemWrapper itemWrapper : schema.get("base64BinaryType").getFacets().getEnumeration()) {
            assertFalse(schema.get("base64BinaryType").validate(itemWrapper, true));
        }
    }
}
