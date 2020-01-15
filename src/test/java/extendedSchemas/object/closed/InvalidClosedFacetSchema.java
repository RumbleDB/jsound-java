package extendedSchemas.object.closed;

import base.BaseTest;
import jsound.exceptions.ClosedSetBackToFalseException;
import org.junit.Test;

import java.io.IOException;

public class InvalidClosedFacetSchema extends BaseTest {

    @Test(expected = ClosedSetBackToFalseException.class)
    public void closedSetBackToFalse() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/object/closed/invalidClosedSchema.json",
            "object/closed/objectFile.json",
            false
        );
    }
}
