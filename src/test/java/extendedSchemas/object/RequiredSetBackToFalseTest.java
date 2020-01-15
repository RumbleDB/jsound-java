package extendedSchemas.object;

import base.BaseTest;
import jsound.exceptions.RequiredSetBackToFalseException;
import org.junit.Test;

import java.io.IOException;

public class RequiredSetBackToFalseTest extends BaseTest {
    @Test(expected = RequiredSetBackToFalseException.class)
    public void requiredSetBackToFalse() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/object/requiredSetBackToFalseSchema.json",
                "object/objectFile.json",
                false
        );
    }
}
