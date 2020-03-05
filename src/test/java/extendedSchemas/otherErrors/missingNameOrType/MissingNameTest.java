package extendedSchemas.otherErrors.missingNameOrType;

import base.BaseTest;
import jsound.exceptions.MissingNameOrTypeException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class MissingNameTest extends BaseTest {

    @Test(expected = MissingNameOrTypeException.class)
    public void missingNameTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/otherErrors/missingNameOrType/missingNameSchema.json",
                "targetType",
                false
        );
    }
}
