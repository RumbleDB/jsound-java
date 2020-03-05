package extendedSchemas.otherErrors.alreadyExistingType;

import base.BaseTest;
import jsound.exceptions.AlreadyExistingTypeException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class AlreadyExistingTypeTest extends BaseTest {

    @Test(expected = AlreadyExistingTypeException.class)
    public void missingKindTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/otherErrors/alreadyExistingType/schema.json",
                "targetType",
                false
        );
    }
}
