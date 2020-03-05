package extendedSchemas.atomicTypes.string;

import base.BaseTest;
import jsound.exceptions.InvalidSchemaException;
import jsound.typedescriptors.object.FieldDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;

public class InvalidDefaultTest extends BaseTest {

    @Test(expected = InvalidSchemaException.class)
    public void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/string/invalidDefaultSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void validateDefaultValues() {
        for (FieldDescriptor fieldDescriptor : schema.get("stringObj").getFacets().getObjectContent().values()) {
            assertFalse(
                fieldDescriptor.getTypeOrReference()
                    .getTypeDescriptor()
                    .validate(fieldDescriptor.getDefaultValue(), false)
            );
        }
    }
}
