package extendedSchemas.atomicTypes.hexBinary.enumeration;

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
                "extendedSchemas/atomicTypes/hexBinary/invalidEnumInSchema.json",
                "atomicTypes/hexBinary/enumeration/hexBinaryEnumeration.json",
                false
        );
    }

    @Test
    public void validateEnumValues() {
        for (ItemWrapper itemWrapper : schema.get("hexBinaryType").getFacets().getEnumeration()) {
            assertFalse(schema.get("hexBinaryType").validate(itemWrapper, true));
        }
    }
}
