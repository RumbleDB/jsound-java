package extendedSchemas.otherErrors.missingNameOrType;

import base.BaseTest;
import jsound.exceptions.MissingNameOrTypeException;
import org.junit.Test;

import java.io.IOException;

public class MissingNameTest extends BaseTest {

    @Test(expected = MissingNameOrTypeException.class)
    public void missingNameTest() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/otherErrors/missingNameOrType/missingNameSchema.json",
                "object/objectFile.json",
                false
        );
    }
}
