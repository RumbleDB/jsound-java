package extendedSchemas.otherErrors.overrideBuiltinType;

import base.BaseTest;
import jsound.exceptions.OverrideBuiltinTypeException;
import org.junit.Test;

import java.io.IOException;

public class OverrideBuiltinTypeTest extends BaseTest {

    @Test(expected = OverrideBuiltinTypeException.class)
    public void missingKindTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/otherErrors/overrideBuiltinType/schema.json",
            "object/objectFile.json",
            false
        );
    }
}
