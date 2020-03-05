package extendedSchemas.otherErrors.cycleInBaseType;

import base.BaseTest;
import jsound.exceptions.CycleInBasetypeException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class CycleInBaseTypeTest extends BaseTest {

    @Test(expected = CycleInBasetypeException.class)
    public void missingKindTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/otherErrors/cycleInBaseType/schema.json",
                "targetType",
                false
        );
    }
}
