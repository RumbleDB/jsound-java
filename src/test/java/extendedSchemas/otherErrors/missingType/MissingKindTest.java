package extendedSchemas.otherErrors.missingType;

import base.BaseTest;
import jsound.exceptions.MissingKindException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class MissingKindTest extends BaseTest {

    @Test(expected = MissingKindException.class)
    public void missingKindTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/otherErrors/missingKind/schema.json",
                "targetType",
                false
        );
    }
}
