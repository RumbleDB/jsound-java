package extendedSchemas.otherErrors.missingNameOrType;

import base.BaseTest;
import jsound.exceptions.MissingNameOrTypeException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class MissingTypeTest extends BaseTest {

    @Test(expected = MissingNameOrTypeException.class)
    public void missingTypeTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/otherErrors/missingNameOrType/missingTypeSchema.json",
                "targetType",
                false
        );
    }
}
