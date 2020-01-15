package extendedSchemas.otherErrors.missingType;

import base.BaseTest;
import jsound.exceptions.MissingKindException;
import org.junit.Test;

import java.io.IOException;

public class MissingKindTest extends BaseTest {

    @Test(expected = MissingKindException.class)
    public void missingKindTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/otherErrors/missingKind/schema.json",
            "object/objectFile.json",
            false
        );
    }
}
