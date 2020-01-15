package extendedSchemas.otherErrors.cycleInBaseType;

import base.BaseTest;
import jsound.exceptions.CycleInBasetypeException;
import org.junit.Test;

import java.io.IOException;

public class CycleInBaseTypeTest extends BaseTest {

    @Test(expected = CycleInBasetypeException.class)
    public void missingKindTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/otherErrors/cycleInBaseType/schema.json",
            "object/objectFile.json",
            false
        );
    }
}
