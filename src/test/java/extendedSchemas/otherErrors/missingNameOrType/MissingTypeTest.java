package extendedSchemas.otherErrors.missingNameOrType;

import base.BaseTest;
import jsound.exceptions.MissingNameOrTypeException;
import org.junit.Test;

import java.io.IOException;

public class MissingTypeTest extends BaseTest {

    @Test(expected = MissingNameOrTypeException.class)
    public void missingTypeTest() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/otherErrors/missingNameOrType/missingTypeSchema.json",
                "object/objectFile.json",
                false
        );
    }
}
