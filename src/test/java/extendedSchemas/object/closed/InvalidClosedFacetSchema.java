package extendedSchemas.object.closed;

import base.BaseTest;
import jsound.exceptions.ClosedSetBackToFalseException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class InvalidClosedFacetSchema extends BaseTest {

    @Test(expected = ClosedSetBackToFalseException.class)
    public void closedSetBackToFalse() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/object/closed/invalidClosedSchema.json",
                "targetType",
                false
        );
    }
}
