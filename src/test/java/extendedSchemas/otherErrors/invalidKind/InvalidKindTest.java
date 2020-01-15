package extendedSchemas.otherErrors.invalidKind;

import base.BaseTest;
import jsound.exceptions.InvalidKindException;
import org.junit.Test;

import java.io.IOException;

public class InvalidKindTest extends BaseTest {

    @Test(expected = InvalidKindException.class)
    public void missingKindTest() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/otherErrors/invalidKind/schema.json",
                "object/objectFile.json",
                false
        );
    }
}
