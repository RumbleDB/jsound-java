package extendedSchemas.otherErrors.inconsistentBasetype;

import base.BaseTest;
import jsound.exceptions.InconsistentBaseTypeException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class InconsistentBasetypeTest extends BaseTest {

    @Test(expected = InconsistentBaseTypeException.class)
    public void inconsistentBaseTypeTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/otherErrors/inconsistentBasetype/schema.json",
                "targetType",
                false
        );
    }
}
