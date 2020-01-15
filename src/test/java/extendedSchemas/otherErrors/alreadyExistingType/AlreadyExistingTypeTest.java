package extendedSchemas.otherErrors.alreadyExistingType;

import base.BaseTest;
import jsound.exceptions.AlreadyExistingTypeException;
import org.junit.Test;

import java.io.IOException;

public class AlreadyExistingTypeTest extends BaseTest {

    @Test(expected = AlreadyExistingTypeException.class)
    public void missingKindTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/otherErrors/alreadyExistingType/schema.json",
            "object/objectFile.json",
            false
        );
    }
}
