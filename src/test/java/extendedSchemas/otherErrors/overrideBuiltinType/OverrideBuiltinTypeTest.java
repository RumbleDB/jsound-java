package extendedSchemas.otherErrors.overrideBuiltinType;

import base.BaseTest;
import jsound.exceptions.OverrideBuiltinTypeException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class OverrideBuiltinTypeTest extends BaseTest {

    @Test(expected = OverrideBuiltinTypeException.class)
    public void missingKindTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/otherErrors/overrideBuiltinType/schema.json",
                "targetType",
                false
        );
    }
}
