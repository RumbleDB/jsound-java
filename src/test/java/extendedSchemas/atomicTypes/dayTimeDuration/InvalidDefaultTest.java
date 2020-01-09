package extendedSchemas.atomicTypes.dayTimeDuration;

import base.BaseTest;
import jsound.exceptions.InvalidSchemaException;
import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;

public class InvalidDefaultTest extends BaseTest {

    @Test(expected = InvalidSchemaException.class)
    public void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/atomicTypes/dayTimeDuration/invalidDefaultSchema.json",
                "atomicTypes/dayTimeDuration/enumeration/dayTimeDurationEnumeration.json",
                false
        );
    }

    @Test
    public void validateDefaultValues() {
        for (FieldDescriptor fieldDescriptor : schema.get("dayTimeDurationObj").getFacets().getObjectContent().values()) {
            assertFalse(fieldDescriptor.getTypeOrReference().getTypeDescriptor().validate(fieldDescriptor.getDefaultValue(), false));
        }
    }
}