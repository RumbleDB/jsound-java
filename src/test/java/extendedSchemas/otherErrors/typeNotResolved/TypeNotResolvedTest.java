package extendedSchemas.otherErrors.typeNotResolved;

import base.BaseTest;
import jsound.exceptions.TypeNotResolvedException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class TypeNotResolvedTest extends BaseTest {

    @Test(expected = TypeNotResolvedException.class)
    public void typeNotResolvedTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/otherErrors/typeNotResolved/schema.json",
                "targetType",
                false
        );
    }
}
