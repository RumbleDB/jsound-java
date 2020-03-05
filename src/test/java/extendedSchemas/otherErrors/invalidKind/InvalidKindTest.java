package extendedSchemas.otherErrors.invalidKind;

import base.BaseTest;
import jsound.exceptions.InvalidKindException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class InvalidKindTest extends BaseTest {

    @Test(expected = InvalidKindException.class)
    public void missingKindTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/otherErrors/invalidKind/schema.json",
                "targetType",
                false
        );
    }
}
