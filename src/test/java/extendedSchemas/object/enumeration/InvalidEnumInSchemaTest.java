package extendedSchemas.object.enumeration;

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
            "extendedSchemas/object/invalidEnumInSchema.json",
            "object/enumeration/objectEnumeration.json",
            false
        );
    }

    @Test
    public void validateEnumValues() {
        for (ItemWrapper itemWrapper : schema.get("objectType").getFacets().getEnumeration()) {
            assertFalse(schema.get("objectType").validate(itemWrapper, true));
        }
    }
}
